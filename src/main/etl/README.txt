Place your transformation files here:

<!DOCTYPE etl SYSTEM "http://scriptella.javaforge.com/dtd/etl.dtd">
<etl>
  <connection id="file" driver="csv" url="$p_target">
    null_string=
    quote=
    eol=\n
    separator=|
  </connection>
  <connection id="db" driver="com.inservio.tools.etl.driver.sybase.SecureDriver" url="$db1.url" user="$db1.user" password="$db1.pass">
    statement.fetchSize = 1000
  </connection>

  <script connection-id="file">
    cola,colb,colc
  </script>

  <script connection-id="file">
    Asar,Khan,Male
  </script>

  <query connection-id="db">
    select * from TRN_HDR_DBF
  </query>
</etl>
