DROP TABLE Bonos                    CASCADE CONSTRAINTS;
DROP TABLE Comentario               CASCADE CONSTRAINTS;
DROP TABLE Comision_Impuesto        CASCADE CONSTRAINTS;
DROP TABLE Cuenta                   CASCADE CONSTRAINTS;
DROP TABLE Empleado                 CASCADE CONSTRAINTS;
DROP TABLE EstadoCuenta             CASCADE CONSTRAINTS;
DROP TABLE Pago                     CASCADE CONSTRAINTS;
DROP TABLE Propiedad                CASCADE CONSTRAINTS;
DROP TABLE Renta                    CASCADE CONSTRAINTS;
DROP TABLE SedeAgencia              CASCADE CONSTRAINTS;
DROP TABLE Servicios                CASCADE CONSTRAINTS;
DROP TABLE Tarjeta                  CASCADE CONSTRAINTS;
DROP TABLE TipoComision_impuesto    CASCADE CONSTRAINTS;
DROP TABLE TipoCuenta               CASCADE CONSTRAINTS;
DROP TABLE TipoDocumento            CASCADE CONSTRAINTS;
DROP TABLE TipoEmpleado             CASCADE CONSTRAINTS;
DROP TABLE TipoPropiedad            CASCADE CONSTRAINTS;
DROP TABLE TipoServicio             CASCADE CONSTRAINTS;
DROP TABLE TipoTarjeta              CASCADE CONSTRAINTS;
DROP TABLE tipoUsoCuenta	    CASCADE CONSTRAINTS;
DROP TABLE Ubicacion                CASCADE CONSTRAINTS;
DROP TABLE Visita                   CASCADE CONSTRAINTS;
DROP TABLE Impuestos                CASCADE CONSTRAINTS;
DROP TABLE Impuestos_pago           CASCADE CONSTRAINTS;
DROP TABLE tipoUsoPropiedad         CASCADE CONSTRAINTS;

CREATE TABLE TipoCuenta (
  idTipoCuenta number(10) GENERATED ALWAYS AS IDENTITY NOT NULL, 
  Tipo         varchar(255) NOT NULL, 
  
  PRIMARY KEY (idTipoCuenta)
);

CREATE TABLE EstadoCuenta (
  idEstadoCuenta number(10) GENERATED ALWAYS AS IDENTITY NOT NULL, 
  Estado         varchar(255) NOT NULL, 
  
  PRIMARY KEY (idEstadoCuenta)
);

CREATE TABLE TipoDocumento (
  idTipoDocumento number(10) GENERATED ALWAYS AS IDENTITY NOT NULL, 
  Tipo            varchar(255) NOT NULL, 
  
  PRIMARY KEY (idTipoDocumento)
);

CREATE TABLE tipoUsoCuenta (
  idEstadoUso number(10) GENERATED ALWAYS AS IDENTITY NOT NULL,
  TIPO	      varchar(255) NOT NULL,

  PRIMARY KEY (idEstadoUso)
);

CREATE TABLE tipoUsoPropiedad (
  idEstadoUso number(10) GENERATED ALWAYS AS IDENTITY NOT NULL,
  TIPO	      varchar(255) NOT NULL,

  PRIMARY KEY (idEstadoUso)
);

CREATE TABLE Cuenta (
  idUsuario        number(10) GENERATED ALWAYS AS IDENTITY NOT NULL, 
  NombreUsuario    varchar(255) NOT NULL UNIQUE, 
  Contrasena       varchar(255) NOT NULL, 
  Nombre           varchar(255) NOT NULL, 
  Apellido         varchar(255) NOT NULL, 
  Correo           varchar(255) NOT NULL, 
  FechaCreacion    date NOT NULL, 
  NumeroDocumento  varchar(255) NOT NULL, 
  idTipoCuenta     number(10) NOT NULL, 
  idEstadoCuenta   number(10) NOT NULL, 
  idTipoDocumento  number(10) NOT NULL,
  idUsando	   number(10) NOT  NULL, 
  
  PRIMARY KEY (idUsuario),
  
  check(correo like '%@%'),

  FOREIGN KEY (idTipoCuenta) REFERENCES TipoCuenta (idTipoCuenta),
  FOREIGN KEY (idTipoDocumento) REFERENCES TipoDocumento (idTipoDocumento),
  FOREIGN KEY (idEstadoCuenta) REFERENCES EstadoCuenta (idEstadoCuenta),
  FOREIGN KEY (idUsando) REFERENCES tipoUsoCuenta (idEstadoUso)
);

CREATE TABLE TipoEmpleado (
  idTipoEmpleado number(10) GENERATED ALWAYS AS IDENTITY NOT NULL, 
  Tipo           varchar(255) NOT NULL, 
  
  PRIMARY KEY (idTipoEmpleado)
);

CREATE TABLE SedeAgencia (
  NombreSede    varchar(255) NOT NULL, 
  UbicacionSede varchar(255) NOT NULL, 
  
  PRIMARY KEY (NombreSede)
);

CREATE TABLE Empleado (
  idEmpleado     number(10) GENERATED ALWAYS AS IDENTITY NOT NULL, 
  Nombre         varchar(255) NOT NULL, 
  Salario        number(12, 3) NOT NULL, 
  idTipoEmpleado number(10) NOT NULL, 
  NombreAgencia  varchar(255) NOT NULL, 
  
  PRIMARY KEY (idEmpleado),

  FOREIGN KEY (NombreAgencia) REFERENCES SedeAgencia (NombreSede),
  FOREIGN KEY (idTipoEmpleado) REFERENCES TipoEmpleado (idTipoEmpleado)
);

CREATE TABLE TipoPropiedad (
  idTipoPropiedad number(10) GENERATED ALWAYS AS IDENTITY NOT NULL, 
  Tipo            varchar(255) NOT NULL, 
  
  PRIMARY KEY (idTipoPropiedad)
);

CREATE TABLE Ubicacion (
  idUbicacion  number(10) GENERATED ALWAYS AS IDENTITY NOT NULL, 
  Pais         varchar(255) NOT NULL, 
  Municipio    varchar(255) NOT NULL, 
  Departamento varchar(255) NOT NULL, 
  PRIMARY KEY (idUbicacion)
);

CREATE TABLE Propiedad (
  idPropiedad        number(10) GENERATED ALWAYS AS IDENTITY NOT NULL, 
  Direccion          varchar(255) NOT NULL, 
  FechaPublicacion   date NOT NULL, 
  Habitaciones       number(10) NOT NULL, 
  VEI                float(10) NOT NULL, 
  idEstadoOcupacion  number NOT NULL, 
  idEstadoEliminado  number NOT NULL, 
  idUsuarioDueno     number(10) NOT NULL, 
  NombreAgencia      varchar(255) NOT NULL, 
  idTipoPropiedad    number(10) NOT NULL, 
  idUbicacion        number(10) NOT NULL, 
  idUsuarioInquilino number(10),
  tipoUso            number(10) NOT NULL,
  
  PRIMARY KEY (idPropiedad),

  FOREIGN KEY (idUbicacion) REFERENCES Ubicacion (idUbicacion),
  FOREIGN KEY (idTipoPropiedad) REFERENCES TipoPropiedad (idTipoPropiedad),
  FOREIGN KEY (NombreAgencia) REFERENCES SedeAgencia (NombreSede),
  FOREIGN KEY (tipoUso) REFERENCES tipoUsoPropiedad (idEstadoUso),
  
  CONSTRAINT dueno FOREIGN KEY (idUsuarioDueno) REFERENCES Cuenta (idUsuario),
  CONSTRAINT inquilino FOREIGN KEY (idUsuarioInquilino) REFERENCES Cuenta (idUsuario)
);

CREATE TABLE Visita (
  idVvisita   number(10) GENERATED ALWAYS AS IDENTITY NOT NULL, 
  Fecha       date NOT NULL, 
  idUsuario   number(10) NOT NULL, 
  idPropiedad number(10) NOT NULL, 
  
  PRIMARY KEY (idVvisita),

  FOREIGN KEY (idPropiedad) REFERENCES Propiedad (idPropiedad),
  FOREIGN KEY (idUsuario) REFERENCES Cuenta (idUsuario)
 );

CREATE TABLE Comentario (
  idComentario number(10) GENERATED ALWAYS AS IDENTITY NOT NULL, 
  Texto        varchar(255), 
  idVvisita    number(10) NOT NULL, 
  
  PRIMARY KEY (idComentario),

  FOREIGN KEY (idVvisita) REFERENCES Visita (idVvisita)
);

CREATE TABLE Renta (
  idRenta          number(10) GENERATED ALWAYS AS IDENTITY NOT NULL, 
  VTR              float(10) NOT NULL, 
  idPropiedad      number(10) NOT NULL, 
  
  PRIMARY KEY (idRenta),

  FOREIGN KEY (idPropiedad) REFERENCES Propiedad (idPropiedad)
);

CREATE TABLE TipoServicio (
  idTipoServicio number(10) GENERATED ALWAYS AS IDENTITY NOT NULL, 
  NombreServicio varchar(255) NOT NULL, 
  
  PRIMARY KEY (idTipoServicio)
);

CREATE TABLE Servicios (
  idRenta	   number(10) NOT NULL, 
  NumeroInquilinos number(10) NOT NULL, 
  idTipoServicio   number(10) NOT NULL, 
  
  PRIMARY KEY (idRenta, idTipoServicio),

  FOREIGN KEY (idTipoServicio) REFERENCES TipoServicio (idTipoServicio),
  FOREIGN KEY (idRenta) REFERENCES renta (idRenta)
);

CREATE TABLE TipoComision_impuesto (
  idTipoComision_impuesto number(10) GENERATED ALWAYS AS IDENTITY NOT NULL, 
  Tipo                    varchar(255) NOT NULL, 
  Porcentaje              float(10) NOT NULL, 
  
  PRIMARY KEY (idTipoComision_impuesto)
);

CREATE TABLE Comision_Impuesto (
  idComision_impuesto     number(10) GENERATED ALWAYS AS IDENTITY NOT NULL, 
  Valor                   float(10) NOT NULL, 
  idTipoComision_impuesto number(10) NOT NULL, 
  idRenta		  number(10) NOT NULL,
  PRIMARY KEY (idComision_impuesto),

  FOREIGN KEY (idTipoComision_impuesto) REFERENCES TipoComision_impuesto (idTipoComision_impuesto),
  FOREIGN KEY (idRenta) REFERENCES Renta (idRenta)
);

CREATE TABLE TipoTarjeta (
  idTipoTarjeta number(10) GENERATED ALWAYS AS IDENTITY NOT NULL, 
  Tipo          varchar(255) NOT NULL, 
  
  PRIMARY KEY (idTipoTarjeta)
);

CREATE TABLE Tarjeta (
  NumeroTarjeta          number(10) NOT NULL, 
  NombrePoseedorPrimario varchar(255) NOT NULL, 
  MesFechaVencimiento    number(10) NOT NULL, 
  AnoFechaVencimiento    number(10) NOT NULL, 
  idTipoTarjeta          number(10) NOT NULL, 
  
  PRIMARY KEY (NumeroTarjeta),

  FOREIGN KEY (idTipoTarjeta) REFERENCES TipoTarjeta (idTipoTarjeta)
);

CREATE TABLE Pago (
  idPago           number(10) GENERATED ALWAYS AS IDENTITY NOT NULL, 
  Correo           varchar(255) NOT NULL, 
  VTP              float(10) NOT NULL, 
  FechaTransaccion date NOT NULL, 
  idRenta          number(10) NOT NULL, 
  NumeroTarjeta    number(10), 
  
  PRIMARY KEY (idPago),
  
  check(correo like '%@%'),

  FOREIGN KEY (NumeroTarjeta) REFERENCES Tarjeta (NumeroTarjeta),
  FOREIGN KEY (idRenta) REFERENCES Renta (idRenta)
);

CREATE TABLE Impuestos (
  idImpuesto       number(10) GENERATED ALWAYS AS IDENTITY NOT NULL,
  tipo		        varchar(255) NOT NULL,
  Porcentaje       float(10) NOT NULL,
  PRIMARY KEY (idImpuesto)
);

CREATE TABLE Impuestos_Pago (
  idImpuesto       number(10) GENERATED ALWAYS AS IDENTITY NOT NULL,
  Valor                   float(10) NOT NULL,
  idTipoImpuesto number(10) NOT NULL,
  idPago      number(10) NOT NULL,
  PRIMARY KEY (idImpuesto),
  FOREIGN KEY (idTipoImpuesto) REFERENCES Impuestos (idImpuesto),
  FOREIGN KEY (idPago) REFERENCES Pago (idPago)
);

CREATE TABLE Bonos (
  NumeroBono number(10) NOT NULL, 
  ValorBono  float(10) NOT NULL, 
  idPago     number(10) NOT NULL, 
  PRIMARY KEY (NumeroBono),
  
  FOREIGN KEY (idPago) REFERENCES Pago (idPago)
  );



insert into tipocuenta (tipo) values ('propietario');
insert into tipocuenta (tipo) values ('cliente');
insert into tipocuenta (tipo) values ('empleado');

insert into estadoCuenta (estado) values ('activo');
insert into estadoCuenta (estado) values ('inactivo');

insert into tipodocumento (tipo) values ('CC');
insert into tipodocumento (tipo) values ('CE');

insert into tipopropiedad (tipo) values ('Apartamento');
insert into tipopropiedad (tipo) values ('Casa');

insert into tipoUsoCuenta (tipo) values ('Inactivo');
insert into tipoUsoCuenta (tipo) values ('Activo');

insert into tipoUsoPropiedad (tipo) values ('Inactivo');
insert into tipoUsoPropiedad (tipo) values ('Activo');

insert into sedeagencia (nombresede,ubicacionsede) values ('JaverianaS', ' Av. Alberto Lleras Camargo #40 - 62, Bogotá');
insert into sedeagencia (nombresede,ubicacionsede) values ('SedeSuba', 'Subazar');
insert into sedeagencia (nombresede,ubicacionsede) values ('SedeUsaquen', 'Santafe');

insert into ubicacion (pais, municipio,departamento) values ('Colombia','Bogota D.C','Bogota D.C');
insert into ubicacion (pais, municipio,departamento) values ('Colombia','Soacha','Cundinamarca');
insert into ubicacion (pais, municipio,departamento) values ('Colombia','Guaduas','Cundinamarca');
insert into ubicacion (pais, municipio,departamento) values ('Colombia','Ubate','Cundinamarca');
insert into ubicacion (pais, municipio,departamento) values ('Colombia','Funza','Cundinamarca');
insert into ubicacion (pais, municipio,departamento) values ('Colombia','Mosquera','Cundinamarca');
insert into ubicacion (pais, municipio,departamento) values ('Colombia','girardot','Cundinamarca');

insert into tipotarjeta(tipo) values ('MasterCard');
insert into tipotarjeta(tipo) values ('VISA');
insert into tipotarjeta(tipo) values ('BBVA');
insert into tipotarjeta(tipo) values ('Ripley');

insert into tipocomision_impuesto (tipo,porcentaje) values ('Gestion',0.01);
insert into tipocomision_impuesto (tipo,porcentaje) values ('Administracion',0.05);

insert into impuestos (tipo,porcentaje) values ('Retencion de fuente', 0.07);
insert into impuestos (tipo,porcentaje) values ('IVA', 0.16);
insert into impuestos (tipo,porcentaje) values ('ICA', 0.06);

insert into tipoServicio (nombreServicio) values ('piscina');
insert into tipoServicio (nombreServicio) values ('seguridad privada');
insert into tipoServicio (nombreServicio) values ('gimnasio');

insert into Cuenta (nombreusuario,contrasena,nombre,apellido,correo, fechaCreacion, numerodocumento,idtipocuenta,idestadocuenta, idtipodocumento, idUsando) values ('admin',          'abcd','juan','echeverry', 'juan_echeverry@javeriana.edu.co',   date '2019-10-20', 1000613089,1,1,1,1);
insert into Cuenta (nombreusuario,contrasena,nombre,apellido,correo, fechaCreacion, numerodocumento,idtipocuenta,idestadocuenta, idtipodocumento, idUsando) values ('inactiveuser',   'abcd','daniel','rendon', 'user@javeriana.edu.co',              date '2019-10-20', 1059622522,1,2,1,1);
insert into Cuenta (nombreusuario,contrasena,nombre,apellido,correo, fechaCreacion, numerodocumento,idtipocuenta,idestadocuenta, idtipodocumento, idUsando) values ('FernandB',       'abcd','Fernando','Baron', 'user@javeriana.edu.co',             date '2019-10-20', 1155682399,1,1,1,1);
insert into Cuenta (nombreusuario,contrasena,nombre,apellido,correo, fechaCreacion, numerodocumento,idtipocuenta,idestadocuenta, idtipodocumento, idUsando) values ('AndresR',        'abcd','Andres','Rendon', 'user@javeriana.edu.co',              date '2019-10-20', 1853258542,1,1,1,1);
insert into Cuenta (nombreusuario,contrasena,nombre,apellido,correo, fechaCreacion, numerodocumento,idtipocuenta,idestadocuenta, idtipodocumento, idUsando) values ('cliente',        'abcd','daniel','rendon', 'user@javeriana.edu.co',              date '2019-10-20', 1033695482,2,1,1,1);
insert into Cuenta (nombreusuario,contrasena,nombre,apellido,correo, fechaCreacion, numerodocumento,idtipocuenta,idestadocuenta, idtipodocumento, idUsando) values ('camS',           'abcd','Camilo','Suarez', 'user@javeriana.edu.co',              date '2019-10-20', 1023456882,2,1,1,1);
insert into Cuenta (nombreusuario,contrasena,nombre,apellido,correo, fechaCreacion, numerodocumento,idtipocuenta,idestadocuenta, idtipodocumento, idUsando) values ('DanielC',        'abcd','daniel','Castro', 'user@javeriana.edu.co',              date '2019-10-20', 1059698582,2,1,1,1);


insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('4245 Dayton Court',date '2022-01-23', 7, 138, 1, 1, 1, 'SedeSuba', 1, 3, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('36 Arkansas Center',date '2021-06-29', 2, 243.46, 1, 1, 3, 'JaverianaS', 1, 3, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('7 Village Parkway',date '2021-08-09', 4, 600.6, 1, 1, 2, 'SedeUsaquen', 1, 3, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('3 Cottonwood Drive',date '2022-09-26', 2, 111.07, 1, 1, 1, 'SedeSuba', 1, 6, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('3236 Kim Crossing',date '2020-10-19', 2, 199.36, 1, 1, 1, 'JaverianaS', 2, 7, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('19 Fremont Plaza',date '2021-10-22', 2, 115.9, 1, 1, 4, 'SedeUsaquen', 2, 1, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('571 Banding Crossing',date '2021-04-09', 1, 468.64, 1, 1, 3, 'SedeSuba', 1, 3, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('6 Clemons Junction',date '2021-09-26', 2, 148.17, 1, 1, 1, 'SedeUsaquen', 1, 4, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('0 La Follette Parkway',date '2020-03-28', 3, 217.3, 1, 1, 4, 'SedeSuba', 2, 7, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('14 Commercial Junction',date '2020-02-05', 7, 1304.54, 1, 1, 4, 'JaverianaS', 1, 3, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('7836 Manufacturers Terrace',date '2020-04-25', 3, 1506.67, 1, 1, 1, 'SedeUsaquen', 1, 6, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('8 Lindbergh Parkway',date '2020-04-11', 4, 2490.37, 1, 1, 1, 'SedeUsaquen', 1, 7, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('5 Riverside Parkway',date '2022-10-09', 2, 2269.65, 1, 1, 1, 'JaverianaS', 1, 4, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('66397 Dapin Place',date '2020-09-03', 6, 137.66, 1, 1, 4, 'JaverianaS', 1, 1, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('2 Mallory Crossing',date '2022-03-20', 5, 982.79, 1, 1, 2, 'JaverianaS', 1, 1, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('36301 Mcguire Avenue',date '2021-12-21', 2, 209.48, 1, 1, 3, 'SedeUsaquen', 1, 2, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('61840 Graedel Crossing',date '2022-07-26', 7, 124.95, 1, 1, 4, 'JaverianaS', 2, 4, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('4248 Nancy Alley',date '2021-08-05', 7, 650.42, 1, 1, 2, 'SedeSuba', 1, 2, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('3762 Huxley Park',date '2020-06-25', 5, 123.23, 1, 1, 3, 'JaverianaS', 2, 3, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('91696 West Center',date '2020-07-12', 7, 253.85, 1, 1, 4, 'JaverianaS', 2, 7, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('78 Monterey Drive',date '2021-08-20', 1, 182.34, 1, 1, 2, 'SedeSuba', 1, 1, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('2 Fieldstone Road',date '2021-05-29', 2, 213.59, 1, 1, 2, 'SedeSuba', 1, 3, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('2633 Vermont Terrace',date '2022-10-29', 3, 245.06, 1, 1, 3, 'JaverianaS', 2, 1, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('4 Stang Hill',date '2021-11-14', 3, 136.89, 1, 1, 1, 'JaverianaS', 1, 3, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('5312 2nd Alley',date '2022-03-21', 1, 154.86, 1, 1, 2, 'JaverianaS', 1, 7, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('7 Anniversary Place',date '2021-04-03', 1, 205.8, 1, 1, 2, 'SedeUsaquen', 2, 5, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('131 Duke Alley',date '2022-10-12', 2, 172.28, 1, 1, 4, 'SedeSuba', 2, 2, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('75 Parkside Pass',date '2022-02-18', 3, 162.89, 1, 1, 1, 'SedeUsaquen', 1, 1, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('3488 Kropf Place',date '2022-02-02', 5, 852.91, 1, 1, 2, 'SedeUsaquen', 2, 2, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('6 Weeping Birch Lane',date '2022-03-26', 3, 227.56, 1, 1, 3, 'SedeUsaquen', 2, 4, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('7178 Green Pass',date '2022-02-21', 7, 1124.86, 1, 1, 2, 'JaverianaS', 2, 6, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('53 Thackeray Pass',date '2021-07-10', 6, 316.7, 1, 1, 1, 'SedeSuba', 2, 7, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('0 Independence Terrace',date '2020-11-10', 6, 123.46, 1, 1, 4, 'SedeSuba', 1, 2, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('1 Northfield Point',date '2022-04-09', 4, 184.75, 1, 1, 1, 'SedeUsaquen', 1, 2, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('4 Eastlawn Terrace',date '2022-02-07', 3, 221.06, 1, 1, 3, 'SedeUsaquen', 2, 2, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('318 Transport Trail',date '2021-07-17', 3, 470.98, 1, 1, 4, 'JaverianaS', 1, 5, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('72949 Sunbrook Pass',date '2022-09-29', 6, 1615.04, 1, 1, 2, 'JaverianaS', 1, 1, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('1262 Di Loreto Point',date '2021-10-25', 4, 877.76, 1, 1, 1, 'SedeUsaquen', 1, 5, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('4 Chive Way',date '2022-10-26', 3, 902.75, 1, 1, 3, 'SedeUsaquen', 2, 4, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('7247 Merry Circle',date '2022-07-21', 5, 144.13, 1, 1, 4, 'JaverianaS', 2, 4, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('2 Merrick Way',date '2021-04-12', 5, 88.46, 1, 1, 2, 'SedeUsaquen', 1, 4, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('61627 Scoville Alley',date '2021-03-17', 6, 1003.34, 1, 1, 3, 'JaverianaS', 1, 5, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('48 Loomis Center',date '2022-03-23', 2, 183.03, 1, 1, 2, 'SedeSuba', 1, 5, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('1 Clyde Gallagher Court',date '2020-05-09', 5, 255.73, 1, 1, 2, 'SedeUsaquen', 1, 4, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('772 Moulton Park',date '2021-05-04', 6, 198.03, 1, 1, 4, 'JaverianaS', 2, 2, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('7708 Hanson Hill',date '2021-09-02', 5, 567.4, 1, 1, 3, 'SedeUsaquen', 1, 2, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('88645 Sherman Point',date '2021-09-07', 2, 186.8, 1, 1, 3, 'SedeUsaquen', 2, 6, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('2343 Sachtjen Plaza',date '2020-08-24', 4, 390.75, 1, 1, 1, 'SedeSuba', 1, 6, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('2988 Arizona Point',date '2020-03-06', 7, 148.03, 1, 1, 2, 'JaverianaS', 2, 6, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('55755 Oxford Street',date '2021-05-08', 6, 169.63, 1, 1, 3, 'SedeSuba', 2, 7, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('7 Melvin Place',date '2020-04-01', 4, 1075.5, 1, 1, 2, 'SedeSuba', 1, 5, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('967 Jana Way',date '2020-04-15', 4, 182.58, 1, 1, 3, 'SedeUsaquen', 1, 5, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('18860 Mandrake Crossing',date '2022-03-23', 6, 283.72, 1, 1, 1, 'SedeSuba', 1, 2, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('02843 Fairfield Point',date '2022-08-28', 1, 1173.29, 1, 1, 3, 'JaverianaS', 1, 2, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('54388 Haas Crossing',date '2020-08-28', 7, 1325.9, 1, 1, 4, 'SedeUsaquen', 1, 6, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('3 Graedel Street',date '2022-10-11', 3, 202.88, 1, 1, 3, 'SedeSuba', 1, 4, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('13590 Loeprich Court',date '2022-09-26', 3, 135.45, 1, 1, 1, 'SedeSuba', 2, 6, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('5387 Village Green Hill',date '2020-07-09', 1, 1070.68, 1, 1, 3, 'SedeUsaquen', 2, 6, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('6167 Almo Trail',date '2020-08-30', 2, 191.17, 1, 1, 3, 'JaverianaS', 1, 7, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('20 Scofield Lane',date '2021-12-30', 3, 1050.57, 1, 1, 1, 'SedeSuba', 1, 6, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('599 Blackbird Parkway',date '2021-06-22', 7, 167.81, 1, 1, 3, 'SedeSuba', 2, 2, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('5 Hayes Terrace',date '2021-10-25', 1, 429.6, 1, 1, 4, 'SedeSuba', 1, 4, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('236 Sundown Place',date '2021-01-12', 2, 436.12, 1, 1, 1, 'SedeSuba', 1, 3, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('0425 Lillian Street',date '2020-09-27', 5, 476.16, 1, 1, 3, 'JaverianaS', 1, 6, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('7770 Bunting Place',date '2021-04-01', 4, 1097.82, 1, 1, 2, 'SedeSuba', 2, 6, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('64 Stuart Way',date '2022-09-09', 1, 646.71, 1, 1, 4, 'JaverianaS', 2, 3, 1);
insert into propiedad (direccion, fechapublicacion, habitaciones, vei, idestadoocupacion, idestadoeliminado, idusuariodueno, nombreagencia, idtipopropiedad, idubicacion, tipoUso) values ('38 Nova Circle',date '2020-02-05', 3, 1071.65, 1, 1, 2, 'SedeSuba', 1, 6, 1);