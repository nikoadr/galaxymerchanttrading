

CREATE TABLE public.comodity (
    id integer NOT NULL,
    comodity_name character varying(50),
    comodity_value double precision
);


ALTER TABLE public.comodity OWNER TO postgres;

--
-- TOC entry 205 (class 1259 OID 31000)
-- Name: comodity_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.comodity_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.comodity_id_seq OWNER TO postgres;

--
-- TOC entry 2937 (class 0 OID 0)
-- Dependencies: 205
-- Name: comodity_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.comodity_id_seq OWNED BY public.comodity.id;


--
-- TOC entry 2808 (class 2604 OID 31005)
-- Name: comodity id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comodity ALTER COLUMN id SET DEFAULT nextval('public.comodity_id_seq'::regclass);


--
-- TOC entry 2931 (class 0 OID 31002)
-- Dependencies: 206
-- Data for Name: comodity; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.comodity (id, comodity_name, comodity_value) VALUES (2, 'Gold', 14450);
INSERT INTO public.comodity (id, comodity_name, comodity_value) VALUES (3, 'Iron', 195.5);
INSERT INTO public.comodity (id, comodity_name, comodity_value) VALUES (4, 'Silver', 17);


--
-- TOC entry 2938 (class 0 OID 0)
-- Dependencies: 205
-- Name: comodity_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.comodity_id_seq', 4, true);


