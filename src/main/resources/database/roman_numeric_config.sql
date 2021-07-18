
CREATE TABLE public.roman_numeric_config (
    id integer DEFAULT nextval('public.roman_numeric_config_seq'::regclass) NOT NULL,
    num_name character varying(50),
    num_code character varying(50),
    num_value integer
);


ALTER TABLE public.roman_numeric_config OWNER TO postgres;

--
-- TOC entry 2930 (class 0 OID 30984)
-- Dependencies: 204
-- Data for Name: roman_numeric_config; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.roman_numeric_config (id, num_name, num_code, num_value) VALUES (1, 'glob', 'I', 1);
INSERT INTO public.roman_numeric_config (id, num_name, num_code, num_value) VALUES (2, 'prok', 'V', 5);
INSERT INTO public.roman_numeric_config (id, num_name, num_code, num_value) VALUES (4, 'tegj', 'L', 50);
INSERT INTO public.roman_numeric_config (id, num_name, num_code, num_value) VALUES (3, 'pish', 'X', 10);



