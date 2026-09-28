class ProductoCard extends HTMLElement {

    constructor() {

        super();

        const shadow = this.attachShadow({
            mode: "open"
        });

        const template = document.createElement("template");

        template.innerHTML = `
            <style>

                article {
                    border: 1px solid #999;
                    padding: 15px;
                    border-radius: 8px;
                    height: 100%;
                    background: white;
                }

                h3 {
                    margin-top: 0;
                }

                button {
                    margin-right: 5px;
                    padding: 5px 10px;
                    cursor: pointer;
                }

                h3 {
                    margin-top: 0;
                }

                button {
                    margin-right: 5px;
                    padding: 5px 10px;
                }

            </style>

            <article>

                <h3>
                    <slot name="nombre">
                        Producto
                    </slot>
                </h3>

                <p>
                    Precio:
                    <slot name="precio">
                        $0.00
                    </slot>
                </p>

                <p>
                    <slot name="comentarios">
                        Sin comentarios
                    </slot>
                </p>

                <button id="editar">
                    Editar
                </button>

                <button id="eliminar">
                    Eliminar
                </button>

            </article>
        `;

        shadow.appendChild(
            template.content.cloneNode(true)
        );


        this.botonEditar =
            shadow.querySelector("#editar");

        this.botonEliminar =
            shadow.querySelector("#eliminar");


        this.botonEditar.addEventListener(
            "click",
            () => {

                this.editar();

            }
        );


        this.botonEliminar.addEventListener(
            "click",
            () => {

                this.eliminar();

            }
        );

    }


    editar() {

        const id =
            this.getAttribute("data-id");

        const nombreActual =
            this.querySelector('[slot="nombre"]').textContent;

        const precioActual =
            this.querySelector('[slot="precio"]').textContent
                .replace("$", "");


        const nuevoNombre =
            prompt(
                "Nuevo nombre:",
                nombreActual
            );


        if (nuevoNombre === null) {
            return;
        }


        const nuevoPrecio =
            prompt(
                "Nuevo precio:",
                precioActual
            );


        if (nuevoPrecio === null) {
            return;
        }


        const productoActualizado = {

            nombre: nuevoNombre,

            precioSugerido:
                parseFloat(nuevoPrecio),

            activo: true,

            comentarios:
                this.querySelector(
                    '[slot="comentarios"]'
                ).textContent

        };


        fetch(`api/productos/${id}`, {

            method: "PUT",

            headers: {
                "Content-Type": "application/json"
            },

            body:
                JSON.stringify(productoActualizado)

        })
        .then(respuesta => {

            if (!respuesta.ok) {

                throw new Error(
                    "No se pudo actualizar el producto"
                );

            }

            return respuesta.json();

        })
        .then(() => {

            this.dispatchEvent(
                new CustomEvent(
                    "producto-actualizado",
                    {
                        bubbles: true
                    }
                )
            );

        })
        .catch(error => {

            alert(error.message);

        });

    }


    eliminar() {

        const id =
            this.getAttribute("data-id");


        const confirmar =
            confirm(
                "¿Desea eliminar este producto?"
            );


        if (!confirmar) {
            return;
        }


        fetch(`api/productos/${id}`, {

            method: "DELETE"

        })
        .then(respuesta => {

            if (!respuesta.ok) {

                throw new Error(
                    "No se pudo eliminar el producto"
                );

            }

        })
        .then(() => {

            this.dispatchEvent(
                new CustomEvent(
                    "producto-eliminado",
                    {
                        bubbles: true
                    }
                )
            );

        })
        .catch(error => {

            alert(error.message);

        });

    }

}


customElements.define(
    "producto-card",
    ProductoCard
);

export { ProductoCard };