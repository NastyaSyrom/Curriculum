create table IF NOT EXISTS Studgroup (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    code_Studgroup varchar(50),
    faculty_id int,
    educationalProgram_id int
);
create table IF NOT EXISTS Discipline  (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name varchar(200),
	semester int,
	attestationForm_id int,
	formOfTraining_id int,
	faculty_id int
);

create table IF NOT EXISTS Faculty(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    fullName varchar(250),
	abbreviatedName varchar(20)
);

create table IF NOT EXISTS FormOfTraining(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	name varchar(20)
);

create table IF NOT EXISTS EducationalProgram(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	name varchar(250),
	formOfTraining_id int,
	faculty_id int
);


create table IF NOT EXISTS AttestationForm(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	name varchar(50)
);

create table IF NOT EXISTS AssessmentTemplate (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	attestationForm_id int,
	assessTemplate varchar(50)
);

INSERT INTO Faculty(fullName, abbreviatedName)
    values
        ('Факультет информационных технологий и компьютерной безопасности', 'ФИТКБ'),
		('Культурно-просветительский факультет', 'КПФ');


INSERT INTO AttestationForm(name)
    values
    ('Зачет'),
	('Дифференцированный зачет'),
	('Экзамен');


INSERT INTO EducationalProgram(name)
    values
	('09.03.03 Прикладная информатика'),
	('05.13.01 Системный анализ, управление и обработка информации (в информационных и технических системах)');

INSERT INTO Discipline(name, semester, attestationForm_id, formOfTraining_id, faculty_id)
    values
        ('Математика', 1, 1, 1, 1),
        ('Иностранный язык', 1, 1, 1, 1),
        ('История', 1, 1, 1, 1),
        ('Физическая культура и спорт', 1, 1, 1, 1),
        ('Информатика', 1, 2, 1, 1),
        ('Русский язык и деловое общение', 1, 1, 1, 1),
  ('Высокоуровневые методы программирования', 1, 1, 1, 1),
        ('WEB-технологии', 1, 2, 1,1),
  ('Элективные дисциплины по физической культуре и спорту', 1, 1, 1, 1),
        ('WEB-технологии', 1, 1, 1, 1),
  ('Математика', 2, 2, 1, 2),
        ('Высокоуровневые методы программирования', 2, 3, 1, 2),
  ('Иностранный язык', 2, 2, 1, 2),
        ('Исследование операций и методы оптимизации', 2, 2, 1, 2),
  ('Вычислительные системы, сети и телекоммуникации', 2, 1, 1, 2),
        ('Основы алгоритмизации и структур данных', 2, 1, 2, 2),
  ('Элективные дисциплины по физической культуре и спорту', 2, 1, 2, 2);


INSERT INTO STUDGROUP(code_Studgroup, faculty_id, educationalProgram_id)
    values
        ('бПИЭ-201', 1, 1),
		('бПИЭ-202', 1, 1),
			('аИВ-212', 2, 2),
			('збПИЭ-191', 2, 2);

INSERT INTO FormOfTraining(name)
    values
        ('Очная'),
	('Заочная');