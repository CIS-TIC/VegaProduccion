# VEGA
# Software de Visionade de Espacios, Reserva y Gaestión de Aulas

VEGA es una aplicación con interfaz web que está diseñada para  realizar la generación y la gestión de reservas relativas a las aulas  ubicadas en el edificio “Ossorio” de la Escuela de Especialidades Fundamentales “Antonio de “Escaño”. Además de esto, también mantiene actualizada una relación de aulas de dicho edificio, detallando  sus características principales, y permite la posibilidad de añadir nuevas aulas, y eliminar o modificar las ya existentes conforme a los cambios que pudieran producirse a lo largo del tiempo.

## Instalación de VEGA
Partiendo de un servidor docker con contenedores NGINX, OpenJDK y MySQL, el VEGA se despliega:

Lo primero será descargar todos los archivos de la aplicación de este repositorio (preferiblemente en la ruta /home/e2t/VEGA):
```
sudo git clone https://github.com/CIS-TIC/VegaProduccion
```

A continuación realizar una limpieza del paquete:
```
sudo mvn package clean
```
Para después compilarlo:
```
sudo mvn package
```

Luego copiaremos el archivo .war que se nos acaba de crear a las ubicaciones necesarias para su funcionamiento:
```
sudo cp <archivo.war> /usr/src/myapp
sudo cp <archivo.war> /home/e2t/SVERGA/openjdk/
```

Por último lanzaremos los contenedores con el docker-compose.yml ubicado en /home/e2t/SVERGA
```
sudo docker-compose -d --build
```

Con los servidores de la red Ossorio ya estaríamos listos para visitar la aplicación usando la ruta e2t.eeae.es en el navegador.