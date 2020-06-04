--
-- PostgreSQL database dump
--

-- Dumped from database version 10.11
-- Dumped by pg_dump version 10.11

-- Started on 2020-01-29 16:08:36

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 1 (class 3079 OID 12924)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2837 (class 0 OID 0)
-- Dependencies: 1
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 198 (class 1259 OID 16407)
-- Name: News; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."News" (
    id bigint NOT NULL,
    title character(30) NOT NULL,
    short_text character(100) NOT NULL,
    full_text character(2000) NOT NULL,
    creation_date date NOT NULL,
    modification_date date NOT NULL
);


ALTER TABLE public."News" OWNER TO postgres;

--
-- TOC entry 196 (class 1259 OID 16394)
-- Name: author; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.author (
    id bigint NOT NULL,
    name character varying(30) NOT NULL,
    surname character varying(30) NOT NULL
);


ALTER TABLE public.author OWNER TO postgres;

--
-- TOC entry 197 (class 1259 OID 16399)
-- Name: news_author; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.news_author (
    author_id bigint NOT NULL,
    news_id bigint NOT NULL
);


ALTER TABLE public.news_author OWNER TO postgres;

--
-- TOC entry 199 (class 1259 OID 16422)
-- Name: news_tag; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.news_tag (
    news_id bigint NOT NULL,
    tag_id bigint NOT NULL
);


ALTER TABLE public.news_tag OWNER TO postgres;

--
-- TOC entry 202 (class 1259 OID 16447)
-- Name: roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.roles (
    user_id bigint NOT NULL,
    role_name character(30) NOT NULL
);


ALTER TABLE public.roles OWNER TO postgres;

--
-- TOC entry 200 (class 1259 OID 16430)
-- Name: tag; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tag (
    id bigint NOT NULL,
    name character(30)
);


ALTER TABLE public.tag OWNER TO postgres;

--
-- TOC entry 201 (class 1259 OID 16440)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id bigint NOT NULL,
    name character(20) NOT NULL,
    surname character(20) NOT NULL,
    login character(30) NOT NULL,
    password character(30) NOT NULL
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 2698 (class 2606 OID 16414)
-- Name: News News_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."News"
    ADD CONSTRAINT "News_pkey" PRIMARY KEY (id);


--
-- TOC entry 2694 (class 2606 OID 16398)
-- Name: author author_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.author
    ADD CONSTRAINT author_pkey PRIMARY KEY (id);


--
-- TOC entry 2696 (class 2606 OID 16421)
-- Name: news_author news_author_news_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.news_author
    ADD CONSTRAINT news_author_news_id_key UNIQUE (news_id);


--
-- TOC entry 2700 (class 2606 OID 16434)
-- Name: tag tag_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tag
    ADD CONSTRAINT tag_pkey PRIMARY KEY (id);


--
-- TOC entry 2702 (class 2606 OID 16446)
-- Name: users users_login_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_login_key UNIQUE (login);


--
-- TOC entry 2704 (class 2606 OID 16444)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 2705 (class 2606 OID 16402)
-- Name: news_author author_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.news_author
    ADD CONSTRAINT author_id FOREIGN KEY (author_id) REFERENCES public.author(id) ON DELETE CASCADE;


--
-- TOC entry 2706 (class 2606 OID 16415)
-- Name: news_author news_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.news_author
    ADD CONSTRAINT news_id FOREIGN KEY (news_id) REFERENCES public."News"(id) ON DELETE CASCADE NOT VALID;


--
-- TOC entry 2707 (class 2606 OID 16425)
-- Name: news_tag news_tag_news_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.news_tag
    ADD CONSTRAINT news_tag_news_id_fkey FOREIGN KEY (news_id) REFERENCES public."News"(id);


--
-- TOC entry 2708 (class 2606 OID 16435)
-- Name: news_tag news_tag_tag_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.news_tag
    ADD CONSTRAINT news_tag_tag_id_fkey FOREIGN KEY (tag_id) REFERENCES public.tag(id) NOT VALID;


-- Completed on 2020-01-29 16:08:36

--
-- PostgreSQL database dump complete
--

