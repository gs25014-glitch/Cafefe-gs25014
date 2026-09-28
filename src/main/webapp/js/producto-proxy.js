export const crearProductoProxy = (producto) => {

    return new Proxy(producto, {

        get(objeto, propiedad) {

            return objeto[propiedad];

        },

        set(objeto, propiedad, valor) {

            if (propiedad === "precioSugerido") {

                if (valor < 0) {

                    console.warn(
                        "El precio no puede ser negativo."
                    );

                    return false;
                }
            }

            objeto[propiedad] = valor;

            return true;
        }

    });

};