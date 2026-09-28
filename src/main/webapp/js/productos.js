import { ProductoCard } from "./producto-card.js";
import { crearProductoProxy } from "./producto-proxy.js";

const cargarProductos = async () => {

    try {

        const respuesta =
            await fetch("api/productos");


        if (!respuesta.ok) {

            throw new Error(
                "No se pudieron cargar los productos"
            );

        }


        const productos =
            await respuesta.json();
    
        const productosActivos =
            productos.filter(
                producto => producto.activo
            );

        const nombres =
            productosActivos.map(
                producto => producto.nombre
            );

        console.log(
            "Productos activos:",
            nombres
        );
    
        const worker =
            new Worker("./js/producto-worker.js");

        worker.postMessage(productos);

        worker.onmessage = evento => {

            console.log(
                "Cantidad de productos:",
                evento.data.cantidad
            );

            console.log(
                "Suma de precios:",
                evento.data.total
            );
    
            mostrarNotificacion();

            worker.terminate();
        };


        const contenedor =
            document.querySelector("#productos");


        contenedor.innerHTML = "";


        productos.forEach(productoOriginal => {

            const producto =
                crearProductoProxy(productoOriginal);

            const tarjeta =
                document.createElement(
                    "producto-card"
                );


            tarjeta.setAttribute(
                "data-id",
                producto.idProducto
            );


            const nombre =
                document.createElement("span");

            nombre.setAttribute(
                "slot",
                "nombre"
            );

            nombre.textContent =
                producto.nombre;


            const precio =
                document.createElement("span");

            precio.setAttribute(
                "slot",
                "precio"
            );

            precio.textContent =
                `$${producto.precioSugerido}`;


            const comentarios =
                document.createElement("span");

            comentarios.setAttribute(
                "slot",
                "comentarios"
            );

            comentarios.textContent =
                producto.comentarios ||
                "Sin comentarios";


            tarjeta.appendChild(nombre);

            tarjeta.appendChild(precio);

            tarjeta.appendChild(comentarios);


            contenedor.appendChild(tarjeta);

        });


    } catch (error) {

        console.error(error);

    }

};


document.addEventListener(
    "DOMContentLoaded",
    () => {

        cargarProductos();

    }
);


document.addEventListener(
    "producto-actualizado",
    () => {

        cargarProductos();

    }
);


document.addEventListener(
    "producto-eliminado",
    () => {

        cargarProductos();

    }
);

if ("serviceWorker" in navigator) {

    window.addEventListener("load", () => {

        navigator.serviceWorker.register(
            "./js/service-worker.js"
        )
        .then(() => {
            console.log(
                "Service Worker registrado correctamente."
            );
        })
        .catch(error => {
            console.error(
                "Error al registrar el Service Worker:",
                error
            );
        });

    });

}

const mostrarNotificacion = () => {

    if (!("Notification" in window)) {

        console.log(
            "Este navegador no soporta notificaciones."
        );

        return;
    }

    if (Notification.permission === "granted") {

        new Notification(
            "Cafefe",
            {
                body: "Los productos se cargaron correctamente."
            }
        );

        return;
    }

    if (Notification.permission !== "denied") {

        Notification.requestPermission()
            .then(permiso => {

                if (permiso === "granted") {

                    new Notification(
                        "Cafefe",
                        {
                            body:
                                "Los productos se cargaron correctamente."
                        }
                    );

                }

            });

    }

};