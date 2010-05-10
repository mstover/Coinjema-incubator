ds = new
		org.firebirdsql.jdbc.FBWrappingDataSource();
		ds.setDatabase("daVinci/3050:/var/firebird/lazerweb.fdb");
		ds.setMinSize(1);
		ds.setMaxSize(50);
		ds.setIdleTimeoutMinutes(1);
		ds.setBlockingTimeout(20000);
		ds.setPooling(true);
		ds.setUserName("damuser");
		ds.setPassword("r!VAld0");
ds