package com.inservio.tools.etl.driver.sybase;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;
import scriptella.jdbc.JdbcConnection;
import scriptella.jdbc.JdbcException;
import scriptella.spi.ConnectionParameters;

import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by asark on 18/09/2015.
 */
public class SecureDriver extends scriptella.driver.sybase.Driver {
  private final static Logger log = Logger.getLogger(SecureDriver.class.getName());
  private final static String ALGORITHM = "PBEWithMD5AndDES";
  private final static String PASSWORD_KEY_ENV_VARIABLE = System.getProperty("key.env.var","ETLTOOL-KEY");

  private static StandardPBEStringEncryptor enc;

  public SecureDriver() {
    super();
    if (enc==null) {
      EnvironmentStringPBEConfig e = new EnvironmentStringPBEConfig();
      e.setAlgorithm(ALGORITHM);
      e.setPasswordEnvName(PASSWORD_KEY_ENV_VARIABLE);
      enc = new StandardPBEStringEncryptor();
      enc.setConfig(e);
    }
  }

  @Override
  public JdbcConnection connect(ConnectionParameters params) {
    try {
      Pattern pattern = Pattern.compile("(?i)ENC\\((.*)\\)");
      Properties props = new Properties();
      props.putAll(params.getProperties());

      if (params.getUser() != null) {
        props.put("user", params.getUser());
      }
      if (params.getPassword() != null) {
        Matcher matcher = pattern.matcher(params.getPassword());
        if (matcher.find()) {
          props.put("password", enc.decrypt(matcher.group(1)));
        } else {
          props.put("password", params.getPassword());
        }
      }
      return connect(params, props);
    } catch (SQLException e) {
    }
    throw new JdbcException("Unable to obtain connection for URL " + params.getUrl());
  }
}
