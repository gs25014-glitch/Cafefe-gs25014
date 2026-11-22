// Service worker de Cafefe: guarda la aplicacion en Cache Storage para que abra sin conexion.

const CACHE = "cafefe-v1";

// Archivos de la aplicacion que se guardan al instalar.
// IMPORTANTE: cada archivo nuevo que se cree debe agregarse a esta lista.
const ARCHIVOS = [
    "./",
    "index.html",
    "manifest.webmanifest",
    "js/boundary/components/app_model.js",
    "js/boundary/components/caja_model.js",
    "js/boundary/components/cobro_model.js",
    "js/boundary/components/instalador_app.js",
    "js/boundary/components/login_model.js",
    "js/boundary/components/notificacion_emergente.js",
    "js/boundary/components/orden_model.js",
    "js/boundary/components/productos_model.js",
    "js/boundary/components/recepcion_model.js",
    "js/boundary/components/resumen_orden_model.js",
    "js/boundary/components/tarjeta_producto.js",
    "js/boundary/components/tarjeta_simple.js",
    "js/boundary/components/tipo_producto_model.js",
    "js/control/caja_service.js",
    "js/control/default_service.js",
    "js/control/factura.js",
    "js/control/formato.js",
    "js/control/generador_mensajes.js",
    "js/control/gestor_carrito.js",
    "js/control/gestor_sesion.js",
    "js/control/listas_service.js",
    "js/control/login_service.js",
    "js/control/orden_service.js",
    "js/control/producto_service.js",
    "js/control/recepcion_service.js",
    "js/control/tipo_producto_service.js",
    "js/entity/carrito.js",
    "js/entity/cobro.js",
    "js/entity/default_error.js",
    "js/entity/default_response.js",
    "js/entity/mensaje.js",
    "js/entity/orden_detalle.js",
    "js/entity/pagina_cafefe.js",
    "js/entity/producto.js",
    "js/entity/sesion.js",
    "js/entity/tipo_mensaje.js",
    "js/entity/tipo_producto.js",
    "resources/estilos/caja.css",
    "resources/estilos/cobro.css",
    "resources/estilos/login.css",
    "resources/estilos/notificaciones.css",
    "resources/estilos/orden.css",
    "resources/estilos/principal.css",
    "resources/estilos/productos.css",
    "resources/estilos/recepcion.css",
    "resources/estilos/resumen_orden.css",
    "resources/estilos/tarjeta.css",
    "resources/estilos/tipo_producto.css",
    "resources/imagenes/categoria_defecto.svg",
    "resources/imagenes/icono_192.png",
    "resources/imagenes/icono_512.png",
    "resources/imagenes/icono_maskable_512.png",
    "resources/imagenes/logo_cafefe.png"
];

// Al instalar: guarda los archivos de la aplicacion
self.addEventListener("install", (evento) => {
    evento.waitUntil(
        caches.open(CACHE)
            .then((cache) => cache.addAll(ARCHIVOS))
            .then(() => self.skipWaiting())
    );
});

// Al activar: borra caches de versiones anteriores y toma el control de las paginas abiertas
self.addEventListener("activate", (evento) => {
    evento.waitUntil(
        caches.keys()
            .then((nombres) => Promise.all(
                nombres.filter((nombre) => nombre !== CACHE).map((nombre) => caches.delete(nombre))
            ))
            .then(() => self.clients.claim())
    );
});

// Cada peticion GET: primero la red y, si falla (sin conexion), lo que hay guardado
self.addEventListener("fetch", (evento) => {
    let peticion = evento.request;

    if (peticion.method !== "GET") {
        return;
    }
    // BrowserSync solo existe mientras se desarrolla: no se guarda
    if (new URL(peticion.url).pathname.startsWith("/browser-sync")) {
        return;
    }

    evento.respondWith(redPrimero(evento, peticion));
});

async function redPrimero(evento, peticion) {
    let cache = await caches.open(CACHE);

    try {
        let respuesta = await fetch(peticion);
        if (respuesta.status === 200) {
            evento.waitUntil(cache.put(peticion, respuesta.clone()));
        }
        return respuesta;
    } catch (error) {
        let guardada = await cache.match(peticion);
        if (guardada) {
            return guardada;
        }
        // Si es una navegacion y no hay nada guardado, se muestra la aplicacion
        if (peticion.mode === "navigate") {
            let inicio = await cache.match("index.html");
            if (inicio) {
                return inicio;
            }
        }
        throw error;
    }
}