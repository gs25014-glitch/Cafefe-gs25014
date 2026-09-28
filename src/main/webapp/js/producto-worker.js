self.addEventListener("message", evento => {

    const productos = evento.data;

    let total = 0;

    productos.forEach(producto => {

        total += Number(
            producto.precioSugerido
        );

    });

    self.postMessage({
        total: total,
        cantidad: productos.length
    });

});
