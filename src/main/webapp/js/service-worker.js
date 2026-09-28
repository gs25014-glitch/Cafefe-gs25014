const CACHE_NAME = "cafefe-v1";

const ARCHIVOS = [
    "./rest-productos.html",
    "./js/productos.js",
    "./js/producto-card.js",
    "./js/producto-proxy.js"
];

self.addEventListener("install", evento => {

    evento.waitUntil(
        caches.open(CACHE_NAME)
            .then(cache => {
                return cache.addAll(ARCHIVOS);
            })
    );

    self.skipWaiting();
});

self.addEventListener("activate", evento => {

    evento.waitUntil(
        caches.keys()
            .then(claves => {

                return Promise.all(
                    claves
                        .filter(clave => clave !== CACHE_NAME)
                        .map(clave => caches.delete(clave))
                );

            })
    );

    self.clients.claim();
});

self.addEventListener("fetch", evento => {

    evento.respondWith(
        caches.match(evento.request)
            .then(respuestaCache => {

                if (respuestaCache) {
                    return respuestaCache;
                }

                return fetch(evento.request);
            })
    );

});