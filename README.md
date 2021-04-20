# PROYECTO FINAL INTERNET AVANZADO *(PROGRAMACION WEB)*

URL: https://astrocaribbean.tech/


Desarrollado por:
- Samuel Peña M

- Robert Felix García

La Oficina de Planeamiento (OP) de la PUCMM, solicita a la Escuela en Computación y
Telecomunicaciones, su ayuda para realizar una encuesta a la población de la zona norte
del país. La OP pretende dotar al personal de apoyo con teléfonos inteligentes o tabletas
para realizar las encuestas, los cuales pueden no tener acceso de red (Wifi o 3G/4G)
mientras estén realizando el levantamiento, por dicha razón, es necesario incorporar un
mecanismo para almacenar la información en el dispositivo y sincronizar una vez tenga
acceso a la red de datos. Para aprovechar sus habilidades en herramientas de desarrollo
web, la aplicación utilizará las características de HTML5 avanzadas integrada con Javalin.

Objetivos:

- Utilizar las funcionales avanzadas de HTML5. 
- Implementar aplicaciones que trabajen desconectadas y sincronicen cuando dispongan de conexión. 
- Utilizar dispositivos móviles con aplicaciones web.
-----------------------------------------------------------------------------------------
- Requerimientos puntuales:

- PRIMERA VERSION
LINK: https://github.com/waterpolord/progweb-parcial2
1. La aplicación será una aplicación Web.
2. Aplicación Web debe tener un diseño adaptado a dispositivos móviles con una
   plantilla vistosa.
3. Utilizar H2 como base de datos en el servidor.
4. Utilizar ORM como motor de persistencia en el servidor.
5. Utilizar Javalin en el servidor.
6. El formulario de capturará debe procesar las siguientes informaciones: Nombre,
   Sector y Nivel escolar (Básico, Medio, Grado Universitario, Postgrado y Doctorado)
   así como el usuario que registró el formulario.
7. Cada vez que un registro es almacenado, deben guardar la posición de donde se
   realizó del registro (latitud y longitud).
8. El almacenamiento de la información será local y deberán implementar un
   mecanismo que permita sincronizar con el servidor los datos registrados mediante
   WebSocket y los Web Workers.
9. Deben tener una opción que permita listar la información recibida por el cliente en
   el servidor y presentar en un mapa **(Google Maps API o Open Layers)** donde
   fueron realizados los registros.
10. La aplicación debe permitir la creación de usuarios, asignación de roles y autenticar
    una vez para poder registrar los formularios. Deben utilizar la funcionalidad de Web
    Storage para registrar el acceso en caso de no disponer de conectividad durante su
    uso.
11. Debe existir una acción para modificar o borrar un registro antes del enviar al
    servidor.
12. Debe estar disponible la aplicación en una dirección pública, asociada a un nombre
    de un dominio y configurado con certificado digital.
13. La prueba la estaremos realizando vía un teléfono inteligente o tableta.
    
-SEGUNDA VERSION
1. Los formularios procesados por el usuario deben permitir tomar una foto desde el
    equipo, el cual debe ser incluido en la información almacenada de forma local y
    enviada al servidor. La imagen debe ser almacenada y enviada en base 64. Ver la
    siguiente librería para Webcam-easy.
2. Crear un servicio REST y SOAP que realice las siguientes operaciones:
    1. Listado de los formularios publicadas por un usuario.
    2. Creación de formulario con la estructura necesaria incluyendo la imagen en base 64.
3. Para el servicio REST es necesario implementar un esquema de seguridad basado
   en JWT.
4. Crear un cliente en cualquier plataforma elaborado (no consola) que implemente
   las operaciones publicadas por el API REST.
5. Crear un cliente en cualquier plataforma elaborado (no consola) que implemente
   las operaciones publicadas por el API SOAP.
-----------------------------------------------------------------------------------------   



  
