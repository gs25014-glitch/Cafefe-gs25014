--
-- PostgreSQL database dump
--

\restrict 14dwF5RVGuwcwGlCJy8POC51VF8x0LicLH6YZt1H5FiXv6P5l50hEax1Ghv9e7C

-- Dumped from database version 18.6
-- Dumped by pg_dump version 18.6

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: caja; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.caja (
    id_caja uuid NOT NULL,
    nombre character varying(155),
    activo boolean DEFAULT true,
    observaciones text
);


ALTER TABLE public.caja OWNER TO postgres;

--
-- Name: caracteristica; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.caracteristica (
    id_caracteristica uuid NOT NULL,
    id_tipo_caracteristica uuid,
    nombre character varying(155),
    activo boolean,
    observaciones text
);


ALTER TABLE public.caracteristica OWNER TO postgres;

--
-- Name: descuento; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.descuento (
    id_descuento uuid NOT NULL,
    id_tipo_descuento uuid,
    nombre character varying(155),
    fecha_desde timestamp with time zone DEFAULT now(),
    fecha_hasta timestamp with time zone,
    observaciones text
);


ALTER TABLE public.descuento OWNER TO postgres;

--
-- Name: descuento_producto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.descuento_producto (
    id_descuento_producto uuid NOT NULL,
    id_descuento uuid,
    id_producto uuid,
    fecha_desde timestamp with time zone,
    fecha_hasta timestamp with time zone,
    valor integer,
    observaciones text
);


ALTER TABLE public.descuento_producto OWNER TO postgres;

--
-- Name: empleado; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.empleado (
    id_empleado uuid NOT NULL,
    nombre character varying(255),
    apellido character varying(255),
    activo boolean,
    comentarios text
);


ALTER TABLE public.empleado OWNER TO postgres;

--
-- Name: empleado_rol; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.empleado_rol (
    id_empleado_rol uuid NOT NULL,
    id_empleado uuid,
    id_rol uuid,
    activo boolean,
    observaciones text
);


ALTER TABLE public.empleado_rol OWNER TO postgres;

--
-- Name: factura; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.factura (
    id_factura uuid NOT NULL,
    id_caja uuid,
    id_empleado_rol uuid,
    cliente character varying(155),
    fecha_facturacion timestamp with time zone DEFAULT now(),
    estado character varying(20),
    observaciones text
);


ALTER TABLE public.factura OWNER TO postgres;

--
-- Name: factura_orden_producto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.factura_orden_producto (
    id_factura_orden_producto uuid NOT NULL,
    id_factura uuid,
    id_orden_producto uuid,
    precio numeric(8,2),
    observaciones text
);


ALTER TABLE public.factura_orden_producto OWNER TO postgres;

--
-- Name: orden; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.orden (
    id_orden uuid NOT NULL,
    id_empleado_rol uuid,
    fecha_creacion timestamp with time zone DEFAULT now()
);


ALTER TABLE public.orden OWNER TO postgres;

--
-- Name: orden_producto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.orden_producto (
    id_orden_producto uuid NOT NULL,
    id_orden uuid,
    id_producto uuid,
    precio numeric(8,2),
    observaciones text
);


ALTER TABLE public.orden_producto OWNER TO postgres;

--
-- Name: pago; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pago (
    id_pago uuid NOT NULL,
    id_factura uuid,
    estado character varying(20),
    observaciones text
);


ALTER TABLE public.pago OWNER TO postgres;

--
-- Name: pago_detalle; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pago_detalle (
    id_pago_detalle uuid NOT NULL,
    id_pago uuid,
    monto numeric(8,2),
    tipo_pago character varying(20),
    observaciones text,
    referencia_externa text
);


ALTER TABLE public.pago_detalle OWNER TO postgres;

--
-- Name: producto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.producto (
    id_producto uuid NOT NULL,
    nombre character varying(155),
    activo boolean,
    comentarios text,
    precio_sugerido numeric(8,2) NOT NULL
);


ALTER TABLE public.producto OWNER TO postgres;

--
-- Name: producto_caracteristica; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.producto_caracteristica (
    id_producto_caracteristica uuid NOT NULL,
    id_producto uuid,
    id_caracteristica uuid,
    valor text
);


ALTER TABLE public.producto_caracteristica OWNER TO postgres;

--
-- Name: producto_tipo_producto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.producto_tipo_producto (
    id_producto_tipo_producto uuid NOT NULL,
    id_producto uuid,
    id_tipo_producto uuid,
    fecha_creacion timestamp with time zone,
    observaciones text
);


ALTER TABLE public.producto_tipo_producto OWNER TO postgres;

--
-- Name: rol; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.rol (
    id_rol uuid NOT NULL,
    nombre character varying(155),
    activo boolean DEFAULT true,
    observaciones text
);


ALTER TABLE public.rol OWNER TO postgres;

--
-- Name: tipo_caracteristica; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tipo_caracteristica (
    id_tipo_caracteristica uuid NOT NULL,
    nombre character varying(155),
    expresion_regular text DEFAULT '.'::text,
    activo boolean DEFAULT true,
    observaciones text
);


ALTER TABLE public.tipo_caracteristica OWNER TO postgres;

--
-- Name: tipo_descuento; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tipo_descuento (
    id_tipo_descuento uuid NOT NULL,
    nombre character varying,
    activo boolean DEFAULT true,
    descuento_maximo integer,
    observaciones text
);


ALTER TABLE public.tipo_descuento OWNER TO postgres;

--
-- Name: tipo_producto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tipo_producto (
    id_tipo_producto uuid NOT NULL,
    nombre character varying(155),
    activo boolean,
    observaciones text
);


ALTER TABLE public.tipo_producto OWNER TO postgres;

--
-- Data for Name: caja; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.caja (id_caja, nombre, activo, observaciones) FROM stdin;
a031688b-fad1-447b-9692-435f8cfdbe6c	Caja principal	t	
\.


--
-- Data for Name: caracteristica; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.caracteristica (id_caracteristica, id_tipo_caracteristica, nombre, activo, observaciones) FROM stdin;
\.


--
-- Data for Name: descuento; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.descuento (id_descuento, id_tipo_descuento, nombre, fecha_desde, fecha_hasta, observaciones) FROM stdin;
\.


--
-- Data for Name: descuento_producto; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.descuento_producto (id_descuento_producto, id_descuento, id_producto, fecha_desde, fecha_hasta, valor, observaciones) FROM stdin;
\.


--
-- Data for Name: empleado; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.empleado (id_empleado, nombre, apellido, activo, comentarios) FROM stdin;
4207d829-8e38-4035-a84b-70a2de853325	Juan	Armando	t	Empleado
\.


--
-- Data for Name: empleado_rol; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.empleado_rol (id_empleado_rol, id_empleado, id_rol, activo, observaciones) FROM stdin;
22222222-2222-2222-2222-222222222222	4207d829-8e38-4035-a84b-70a2de853325	11111111-1111-1111-1111-111111111111	t	Asignacion inicial del rol de cajero
\.


--
-- Data for Name: factura; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.factura (id_factura, id_caja, id_empleado_rol, cliente, fecha_facturacion, estado, observaciones) FROM stdin;
1bfc4929-a85c-497f-a787-2efd2796a493	a031688b-fad1-447b-9692-435f8cfdbe6c	22222222-2222-2222-2222-222222222222	Consumidor final	2026-09-26 19:40:25.948982-06	PAGADA	\N
14e01c95-21c0-46e9-ac6a-5af9aa099f91	a031688b-fad1-447b-9692-435f8cfdbe6c	22222222-2222-2222-2222-222222222222	Consumidor final	2026-09-26 20:17:24.746542-06	PENDIENTE	\N
8634b2dd-c223-4b5d-bd32-906f612020ba	a031688b-fad1-447b-9692-435f8cfdbe6c	22222222-2222-2222-2222-222222222222	Consumidor final	2026-09-26 20:19:16.986552-06	PENDIENTE	\N
1b04980c-d1bb-476e-9ff7-91f1e49eaa1e	a031688b-fad1-447b-9692-435f8cfdbe6c	22222222-2222-2222-2222-222222222222	Consumidor final	2026-09-26 20:45:31.150716-06	PENDIENTE	\N
\.


--
-- Data for Name: factura_orden_producto; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.factura_orden_producto (id_factura_orden_producto, id_factura, id_orden_producto, precio, observaciones) FROM stdin;
df166795-f55f-47e4-926b-a5b04c7bf9f8	1bfc4929-a85c-497f-a787-2efd2796a493	5f8e15bc-4aa9-4221-87d5-506a940993ca	1.00	\N
4d7e9363-7724-4637-8a7e-5c7800f15a6b	14e01c95-21c0-46e9-ac6a-5af9aa099f91	1dc1331b-93c2-4f1f-bede-f342989d7834	1.00	\N
f1668d61-edd3-4569-a6e6-14a3d9663738	14e01c95-21c0-46e9-ac6a-5af9aa099f91	5c534682-5577-4c9a-841c-2c0ec8dfd8de	3.50	\N
12d462f2-09ae-45f2-b492-57dd76942d30	14e01c95-21c0-46e9-ac6a-5af9aa099f91	4d012ed6-f2cb-4767-921d-86e53bb23a23	1.00	\N
58be4bc7-d355-40db-8258-9a43fa717407	14e01c95-21c0-46e9-ac6a-5af9aa099f91	d3b47ae4-ddf2-486e-a944-517073ed9166	1.00	\N
4af1189b-6c50-4516-88da-7149f9c09d1f	8634b2dd-c223-4b5d-bd32-906f612020ba	21b7a1d4-ee3b-4602-a44d-c23bec850fce	1.00	\N
4a24bf58-6635-4107-9409-629ae5055b72	8634b2dd-c223-4b5d-bd32-906f612020ba	21bd1b5c-63ec-425d-ae8e-fe7f5040ea9d	3.50	\N
b50ff890-7d09-4db0-b590-35fc85cd1733	1b04980c-d1bb-476e-9ff7-91f1e49eaa1e	84425214-a5f6-401f-ada0-c345deec2b66	3.50	\N
bbf92121-3a8e-4027-a716-8ae73a700694	1b04980c-d1bb-476e-9ff7-91f1e49eaa1e	9aba3ac2-2a7f-4fd3-b26c-f5c4a2c7f2fa	1.00	\N
\.


--
-- Data for Name: orden; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.orden (id_orden, id_empleado_rol, fecha_creacion) FROM stdin;
aa2ba644-fb02-481e-9222-e15d2c23a599	22222222-2222-2222-2222-222222222222	2026-09-26 14:03:34.223292-06
5731dbdb-534c-4140-ae43-c44c3a7b398d	22222222-2222-2222-2222-222222222222	2026-09-26 19:33:24.802011-06
3d4ba21b-c338-4563-955b-6f4692c36890	22222222-2222-2222-2222-222222222222	2026-09-26 19:40:25.823477-06
2ac1908e-db0e-4eff-af63-2af0fcabd7de	22222222-2222-2222-2222-222222222222	2026-09-26 20:17:24.520034-06
2630e20c-0419-4c35-90b1-4268766af717	22222222-2222-2222-2222-222222222222	2026-09-26 20:19:16.973753-06
cce0cd43-3edb-4728-8082-80f51825d596	22222222-2222-2222-2222-222222222222	2026-09-26 20:45:31.116093-06
\.


--
-- Data for Name: orden_producto; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.orden_producto (id_orden_producto, id_orden, id_producto, precio, observaciones) FROM stdin;
488f1a3a-d165-46ea-a359-c5c4d61e226b	aa2ba644-fb02-481e-9222-e15d2c23a599	6346b309-7f86-4d5e-b2ac-8eebfea335d3	3.50	\N
81b6cebd-ae6b-472c-9a9c-0f1e90a87cc3	5731dbdb-534c-4140-ae43-c44c3a7b398d	79b18f5c-7797-4970-ac04-18441ae8832e	1.00	\N
5f8e15bc-4aa9-4221-87d5-506a940993ca	3d4ba21b-c338-4563-955b-6f4692c36890	88f9c7ea-94d6-4110-84fd-b9ebf6fb6bf1	1.00	\N
1dc1331b-93c2-4f1f-bede-f342989d7834	2ac1908e-db0e-4eff-af63-2af0fcabd7de	79b18f5c-7797-4970-ac04-18441ae8832e	1.00	\N
5c534682-5577-4c9a-841c-2c0ec8dfd8de	2ac1908e-db0e-4eff-af63-2af0fcabd7de	6346b309-7f86-4d5e-b2ac-8eebfea335d3	3.50	\N
4d012ed6-f2cb-4767-921d-86e53bb23a23	2ac1908e-db0e-4eff-af63-2af0fcabd7de	79b18f5c-7797-4970-ac04-18441ae8832e	1.00	\N
d3b47ae4-ddf2-486e-a944-517073ed9166	2ac1908e-db0e-4eff-af63-2af0fcabd7de	88f9c7ea-94d6-4110-84fd-b9ebf6fb6bf1	1.00	\N
21b7a1d4-ee3b-4602-a44d-c23bec850fce	2630e20c-0419-4c35-90b1-4268766af717	88f9c7ea-94d6-4110-84fd-b9ebf6fb6bf1	1.00	\N
21bd1b5c-63ec-425d-ae8e-fe7f5040ea9d	2630e20c-0419-4c35-90b1-4268766af717	6346b309-7f86-4d5e-b2ac-8eebfea335d3	3.50	\N
9aba3ac2-2a7f-4fd3-b26c-f5c4a2c7f2fa	cce0cd43-3edb-4728-8082-80f51825d596	88f9c7ea-94d6-4110-84fd-b9ebf6fb6bf1	1.00	\N
84425214-a5f6-401f-ada0-c345deec2b66	cce0cd43-3edb-4728-8082-80f51825d596	6346b309-7f86-4d5e-b2ac-8eebfea335d3	3.50	\N
\.


--
-- Data for Name: pago; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.pago (id_pago, id_factura, estado, observaciones) FROM stdin;
36070e82-8909-4f5f-b360-1447badaf9f8	1bfc4929-a85c-497f-a787-2efd2796a493	PAGADO	\N
\.


--
-- Data for Name: pago_detalle; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.pago_detalle (id_pago_detalle, id_pago, monto, tipo_pago, observaciones, referencia_externa) FROM stdin;
bad44710-4c99-4c27-8abc-4307e75919d2	36070e82-8909-4f5f-b360-1447badaf9f8	1.00	EFECTIVO	\N	\N
\.


--
-- Data for Name: producto; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.producto (id_producto, nombre, activo, comentarios, precio_sugerido) FROM stdin;
6346b309-7f86-4d5e-b2ac-8eebfea335d3	Café cappuccino	t	Producto creado desde JSF	3.50
79b18f5c-7797-4970-ac04-18441ae8832e	Café Expreso	t	Producto creado desde Cafefe	4.50
88f9c7ea-94d6-4110-84fd-b9ebf6fb6bf1	Café americano	t	Producto creado mediante POST	1.00
\.


--
-- Data for Name: producto_caracteristica; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.producto_caracteristica (id_producto_caracteristica, id_producto, id_caracteristica, valor) FROM stdin;
\.


--
-- Data for Name: producto_tipo_producto; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.producto_tipo_producto (id_producto_tipo_producto, id_producto, id_tipo_producto, fecha_creacion, observaciones) FROM stdin;
\.


--
-- Data for Name: rol; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.rol (id_rol, nombre, activo, observaciones) FROM stdin;
11111111-1111-1111-1111-111111111111	Cajero	t	Rol para realizar ventas y registrar ordenes
\.


--
-- Data for Name: tipo_caracteristica; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tipo_caracteristica (id_tipo_caracteristica, nombre, expresion_regular, activo, observaciones) FROM stdin;
\.


--
-- Data for Name: tipo_descuento; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tipo_descuento (id_tipo_descuento, nombre, activo, descuento_maximo, observaciones) FROM stdin;
\.


--
-- Data for Name: tipo_producto; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tipo_producto (id_tipo_producto, nombre, activo, observaciones) FROM stdin;
c9a8e17e-8bf3-47a3-8c3c-0455e24adc38	Postres	t	Productos de repostería
f30bbf09-7c5b-4e7d-92c9-8776009f7e0c	Limpieza	t	Productos de limpieza
\.


--
-- Name: caja pk_caja; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.caja
    ADD CONSTRAINT pk_caja PRIMARY KEY (id_caja);


--
-- Name: caracteristica pk_caracteristica; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.caracteristica
    ADD CONSTRAINT pk_caracteristica PRIMARY KEY (id_caracteristica);


--
-- Name: descuento pk_descuento; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.descuento
    ADD CONSTRAINT pk_descuento PRIMARY KEY (id_descuento);


--
-- Name: descuento_producto pk_descuento_producto; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.descuento_producto
    ADD CONSTRAINT pk_descuento_producto PRIMARY KEY (id_descuento_producto);


--
-- Name: empleado pk_empleado; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.empleado
    ADD CONSTRAINT pk_empleado PRIMARY KEY (id_empleado);


--
-- Name: factura pk_factura; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.factura
    ADD CONSTRAINT pk_factura PRIMARY KEY (id_factura);


--
-- Name: factura_orden_producto pk_factura_orden_producto; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.factura_orden_producto
    ADD CONSTRAINT pk_factura_orden_producto PRIMARY KEY (id_factura_orden_producto);


--
-- Name: empleado_rol pk_id_empleado_rol; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.empleado_rol
    ADD CONSTRAINT pk_id_empleado_rol PRIMARY KEY (id_empleado_rol);


--
-- Name: orden pk_orden; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orden
    ADD CONSTRAINT pk_orden PRIMARY KEY (id_orden);


--
-- Name: orden_producto pk_orden_producto; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orden_producto
    ADD CONSTRAINT pk_orden_producto PRIMARY KEY (id_orden_producto);


--
-- Name: pago pk_pago; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pago
    ADD CONSTRAINT pk_pago PRIMARY KEY (id_pago);


--
-- Name: pago_detalle pk_pago_detalle; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pago_detalle
    ADD CONSTRAINT pk_pago_detalle PRIMARY KEY (id_pago_detalle);


--
-- Name: producto pk_producto; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.producto
    ADD CONSTRAINT pk_producto PRIMARY KEY (id_producto);


--
-- Name: producto_caracteristica pk_producto_caracteristica; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.producto_caracteristica
    ADD CONSTRAINT pk_producto_caracteristica PRIMARY KEY (id_producto_caracteristica);


--
-- Name: producto_tipo_producto pk_producto_tipo_producto; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.producto_tipo_producto
    ADD CONSTRAINT pk_producto_tipo_producto PRIMARY KEY (id_producto_tipo_producto);


--
-- Name: rol pk_rol; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rol
    ADD CONSTRAINT pk_rol PRIMARY KEY (id_rol);


--
-- Name: tipo_caracteristica pk_tipo_caracteristica; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tipo_caracteristica
    ADD CONSTRAINT pk_tipo_caracteristica PRIMARY KEY (id_tipo_caracteristica);


--
-- Name: tipo_descuento pk_tipo_descuento; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tipo_descuento
    ADD CONSTRAINT pk_tipo_descuento PRIMARY KEY (id_tipo_descuento);


--
-- Name: tipo_producto pk_tipo_producto; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tipo_producto
    ADD CONSTRAINT pk_tipo_producto PRIMARY KEY (id_tipo_producto);


--
-- Name: caracteristica fk_caracteristica_tipo_caracteristica; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.caracteristica
    ADD CONSTRAINT fk_caracteristica_tipo_caracteristica FOREIGN KEY (id_tipo_caracteristica) REFERENCES public.tipo_caracteristica(id_tipo_caracteristica) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- Name: descuento_producto fk_descuento_producto_descuento; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.descuento_producto
    ADD CONSTRAINT fk_descuento_producto_descuento FOREIGN KEY (id_descuento) REFERENCES public.descuento(id_descuento) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- Name: descuento_producto fk_descuento_producto_producto; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.descuento_producto
    ADD CONSTRAINT fk_descuento_producto_producto FOREIGN KEY (id_producto) REFERENCES public.producto(id_producto) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- Name: empleado_rol fk_empleado_rol_empleado; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.empleado_rol
    ADD CONSTRAINT fk_empleado_rol_empleado FOREIGN KEY (id_rol) REFERENCES public.rol(id_rol) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- Name: factura fk_factura_caja; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.factura
    ADD CONSTRAINT fk_factura_caja FOREIGN KEY (id_caja) REFERENCES public.caja(id_caja) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- Name: factura fk_factura_empleado_rol; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.factura
    ADD CONSTRAINT fk_factura_empleado_rol FOREIGN KEY (id_empleado_rol) REFERENCES public.empleado_rol(id_empleado_rol) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- Name: factura_orden_producto fk_factura_orden_producto_factura; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.factura_orden_producto
    ADD CONSTRAINT fk_factura_orden_producto_factura FOREIGN KEY (id_factura) REFERENCES public.factura(id_factura) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- Name: factura_orden_producto fk_factura_orden_producto_orden_producto; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.factura_orden_producto
    ADD CONSTRAINT fk_factura_orden_producto_orden_producto FOREIGN KEY (id_orden_producto) REFERENCES public.orden_producto(id_orden_producto) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- Name: orden fk_orden_empleado_rol; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orden
    ADD CONSTRAINT fk_orden_empleado_rol FOREIGN KEY (id_empleado_rol) REFERENCES public.empleado_rol(id_empleado_rol) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- Name: orden_producto fk_orden_producto_orden; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orden_producto
    ADD CONSTRAINT fk_orden_producto_orden FOREIGN KEY (id_orden) REFERENCES public.orden(id_orden) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- Name: orden_producto fk_orden_producto_producto; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orden_producto
    ADD CONSTRAINT fk_orden_producto_producto FOREIGN KEY (id_producto) REFERENCES public.producto(id_producto) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- Name: pago_detalle fk_pago_detalle_pago; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pago_detalle
    ADD CONSTRAINT fk_pago_detalle_pago FOREIGN KEY (id_pago) REFERENCES public.pago(id_pago) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- Name: pago fk_pago_factura; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pago
    ADD CONSTRAINT fk_pago_factura FOREIGN KEY (id_factura) REFERENCES public.factura(id_factura) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- Name: producto_caracteristica fk_producto_caracteristica_caracteristica; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.producto_caracteristica
    ADD CONSTRAINT fk_producto_caracteristica_caracteristica FOREIGN KEY (id_caracteristica) REFERENCES public.caracteristica(id_caracteristica) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- Name: producto_caracteristica fk_producto_caracteristica_producto; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.producto_caracteristica
    ADD CONSTRAINT fk_producto_caracteristica_producto FOREIGN KEY (id_producto) REFERENCES public.producto(id_producto) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- Name: producto_tipo_producto fk_producto_tipo_producto_producto; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.producto_tipo_producto
    ADD CONSTRAINT fk_producto_tipo_producto_producto FOREIGN KEY (id_producto) REFERENCES public.producto(id_producto) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- Name: producto_tipo_producto producto_tipo_producto_id_tipo_producto_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.producto_tipo_producto
    ADD CONSTRAINT producto_tipo_producto_id_tipo_producto_fkey FOREIGN KEY (id_tipo_producto) REFERENCES public.tipo_producto(id_tipo_producto) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- PostgreSQL database dump complete
--

\unrestrict 14dwF5RVGuwcwGlCJy8POC51VF8x0LicLH6YZt1H5FiXv6P5l50hEax1Ghv9e7C

