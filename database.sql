-- Create the database
CREATE DATABASE RS_Medika_Utama;

-- Connect to the database
\c belajar_spring_resful_api;

-- Enable the uuid-ossp extension for generating UUIDs
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create the polyclinics table
CREATE TABLE polyclinics (
    id UUID  PRIMARY KEY,
    slug VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL UNIQUE
);

-- Create the clinics table
CREATE TABLE clinics (
    id UUID  PRIMARY KEY,
    polyclinic_id UUID NOT NULL,
    slug VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL UNIQUE,
    FOREIGN KEY (polyclinic_id) REFERENCES polyclinics(id)
);

-- Create the users table
CREATE TABLE users (
    id UUID  PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    token VARCHAR(100),
    token_expired_at BIGINT
);

-- Create the user_profile table
CREATE TABLE user_profile (
    id UUID  PRIMARY KEY,
    user_id UUID NOT NULL,
    name VARCHAR(100) NOT NULL,
    slug VARCHAR(100) NOT NULL UNIQUE,
    image_url VARCHAR(100),
    date_of_birth DATE,
    address VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Create the doctor_profile table
CREATE TABLE doctor_profile (
    id UUID  PRIMARY KEY,
    user_id UUID NOT NULL,
    slug VARCHAR(100) NOT NULL UNIQUE,
    clinic_id UUID NOT NULL,
    education VARCHAR(100),
    course VARCHAR(100),
    experience VARCHAR(100),
    organization VARCHAR(100),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (clinic_id) REFERENCES clinics(id)
);

-- Create the patient_profile table
CREATE TABLE patient_profile (
    id UUID  PRIMARY KEY,
    user_id UUID NOT NULL,
    noRM VARCHAR(100),
    medical_history TEXT,
    current_treatment TEXT,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Create the roles table
CREATE TABLE roles (
    id UUID  PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- Create the user_roles table
CREATE TABLE user_roles (
    user_id UUID NOT NULL,
    role_id UUID NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- Create the schedules table
CREATE TABLE schedules (
    id UUID  PRIMARY KEY,
    doctor_profile_id UUID NOT NULL,
    day VARCHAR(10) NOT NULL,
    time TIME NOT NULL,
    FOREIGN KEY (doctor_profile_id) REFERENCES doctor_profile(id)
);

-- Create the statuses table
CREATE TABLE statuses (
    id UUID  PRIMARY KEY,
    name VARCHAR(100) NOT NULL -- accepted or rejected
);

-- Create the appointments table
CREATE TABLE appointments (
    id UUID  PRIMARY KEY,
    doctor_profile_id UUID NOT NULL,
    patient_profile_id UUID NOT NULL,
    schedule_id UUID NOT NULL,
    clinic_id UUID NOT NULL,
    status_id UUID NOT NULL,
    date DATE NOT NULL, -- date the appointment is made
    waiting_estimation VARCHAR(100),
    FOREIGN KEY (doctor_profile_id) REFERENCES doctor_profile(id),
    FOREIGN KEY (patient_profile_id) REFERENCES patient_profile(id),
    FOREIGN KEY (schedule_id) REFERENCES schedules(id),
    FOREIGN KEY (clinic_id) REFERENCES clinics(id),
    FOREIGN KEY (status_id) REFERENCES statuses(id)
);

ALTER TABLE appointments
ADD slug varchar(100) NOT NULL UNIQUE;
