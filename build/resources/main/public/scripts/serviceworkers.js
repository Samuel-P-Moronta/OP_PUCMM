/**
 * Ejemplo de un script de service workers.
 */

//Cache activo.
var CACHE_NAME = 'mi-app-cache-v1';
//listado de
var urlsToCache = [
    '/',
    '/assets/bootstrap.css',
    '/assets/bootstrap.min.css',
    '/assets/main.css',
    '/assets/editDelete.js',
    '/assets/klorofil-common.js',
    '/assets/vendor/bootstrap/css/bootstrap.css',
    '/assets/vendor/bootstrap/css/bootstrap.min.css',
    '/assets/vendor/bootstrap/css/bootstrap-theme.css',
    '/assets/vendor/bootstrap/css/bootstrap-theme.min.css',
    '/assets/vendor/bootstrap/js/bootstrap.js',
    '/assets/vendor/bootstrap/js/bootstrap.min.js',
    '/assets/vendor/bootstrap/js/npm.js',
    '/assets/vendor/chartist/css/chartist.css',
    '/assets/vendor/chartist/css/chartist.min.css',
    '/assets/vendor/chartist/js/chartist.js',
    '/assets/vendor/chartist/js/chartist.min.js',
    '/assets/vendor/chartist/js/scss/chartist.scss',
    '/assets/vendor/font-awesome/css/font-awesome.css',
    '/assets/vendor/jquery.js/jquery.js',
    '/assets/vendor/jquery.js/jquery.min.js',
    '/assets/vendor/jquery-easy-pie-chart',
    '/css/myStyle',
    '/scripts/editDelete.js',
    '/scripts/indexDB.js',
    '/scripts/offline.js',
    '/scripts/probando.js',
    '/scripts/validCamps.js',
    '/plantilla',

];
//ruta para fallback.
var fallback = "../public/plantilla.html"

//representa el evento cuando se esta instalando el services workers.
self.addEventListener('install', function(event) {
    console.log('Instalando el Services Worker');
    // Utilizando las promesas para agregar los elementos definidos
    event.waitUntil(
        caches.open(CACHE_NAME) //Utilizando el api Cache definido en la variable.
            .then(function(cache) {
                console.log('Cache abierto');
                return cache.addAll(urlsToCache); //agregando todos los elementos del cache.
            })
    );
});

/**
 * Los Service Workers se actualizan pero no se activan hasta que la quede una site activo
 * que utilice la versión anterior. Para eliminar el problema, una vez activado borramos los cache no utilizado.
 */
self.addEventListener('activate', evt => {
    console.log('Activando el services worker -  Limpiando el cache no utilizado');
    evt.waitUntil(
        caches.keys().then(function(keyList) { //Recupera todos los cache activos.
            return Promise.all(keyList.map(function(key) {
                if (CACHE_NAME.indexOf(key) === -1) { //si no es el cache por defecto borramos los elementos.
                    return caches.delete(key); //borramos los elementos guardados.
                }
            }));
        })
    );
});

/**
 * Representa el evento que se dispara cuando realizamos una solicitud desde la pagina al servidor.
 * Interceptamos el mensaje y podemos verificar si lo tenemos en el cache o no.
 */
self.addEventListener('fetch', event => {
    event.respondWith(
        caches.match(event.request).then(response=>{
            console.log(event);
            //si existe retornamos la petición desde el cache, de lo contrario (retorna undefined) dejamos pasar la solicitud al servidor,
            // lo hacemos con la función fetch pasando un objeto de petición.
            return response || fetch(event.request);
        }).catch(function (){ //En caso de tener un problema con la red, se mostrará el caso
            return caches.match(fallback);
        })
    );
});