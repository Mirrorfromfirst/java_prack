-- Создание базы данных
CREATE DATABASE LegalFirm;

-- Выбор базы данных

-- Таблица "Clients" - информация о клиентах
CREATE TABLE Clients (
                         client_id INT PRIMARY KEY,   -- Уникальный идентификатор клиента
                         name VARCHAR(255) NOT NULL,                  -- Наименование (для организации) или ФИО (для физического лица)
                         type VARCHAR(255) NOT NULL, -- ENUM('organization', 'individual') Тип клиента: организация или физическое лицо
                         contact_person VARCHAR(255),                 -- Контактное лицо (если клиент — организация)
                         phone VARCHAR(20),                           -- Телефон клиента
                         email VARCHAR(255),                          -- Электронная почта клиента
                         address VARCHAR(255)                         -- Адрес клиента
);

-- Таблица "Employees" - информация о служащих
CREATE TABLE Employees (
                           employee_id INT PRIMARY KEY,  -- Уникальный идентификатор служащего
                           full_name VARCHAR(255) NOT NULL,             -- ФИО сотрудника
                           position VARCHAR(255) NOT NULL,              -- Должность
                           email VARCHAR(255),                          -- Электронная почта
                           phone VARCHAR(20),                           -- Телефон
                           address VARCHAR(255),                        -- Адрес
                           education VARCHAR(255)                       -- Образование
);

-- Таблица "Services" - информация о предоставляемых услугах
CREATE TABLE Services (
                          service_id INT PRIMARY KEY,    -- Уникальный идентификатор услуги
                          name VARCHAR(255) NOT NULL,                    -- Название услуги
                          description TEXT,                              -- Описание услуги
                          price DECIMAL(10, 2) NOT NULL                  -- Стоимость услуги
);

-- Таблица "ServiceHistory" - история оказанных услуг клиентам
CREATE TABLE ServiceHistory (
                                client_id INT,                                 -- Идентификатор клиента (ссылка на таблицу Clients)
                                service_id INT,                                -- Идентификатор услуги (ссылка на таблицу Services)
                                employee_id INT,                               -- Идентификатор сотрудника, оказавшего услугу (ссылка на таблицу Employees)
                                service_date DATE,                             -- Дата оказания услуги
                                price_paid DECIMAL(10, 2),                     -- Стоимость оказанной услуги
                                PRIMARY KEY (client_id, service_id, employee_id),  -- Композитный первичный ключ
                                FOREIGN KEY (client_id) REFERENCES Clients(client_id),  -- Связь с клиентами
                                FOREIGN KEY (service_id) REFERENCES Services(service_id),  -- Связь с услугами
                                FOREIGN KEY (employee_id) REFERENCES Employees(employee_id) -- Связь с сотрудниками
);

-- Таблица "Contracts" - информация о договорах между клиентами и фирмой
CREATE TABLE Contracts (
                           client_id INT,                                 -- Идентификатор клиента (ссылка на таблицу Clients)
                           service_id INT,                                -- Идентификатор услуги, по которой заключен договор (ссылка на таблицу Services)
                           start_date DATE,                               -- Дата начала договора
                           end_date DATE,                                 -- Дата окончания договора
                           terms TEXT,                                    -- Условия договора
                           PRIMARY KEY (client_id, service_id),           -- Композитный первичный ключ
                           FOREIGN KEY (client_id) REFERENCES Clients(client_id),  -- Связь с клиентами
                           FOREIGN KEY (service_id) REFERENCES Services(service_id) -- Связь с услугами
);

-- Таблица "Users" - информация о пользователях системы
CREATE TABLE Users (
                       user_id INT PRIMARY KEY,        -- Уникальный идентификатор пользователя
                       username VARCHAR(255) NOT NULL,                 -- Логин пользователя
                       password_hash VARCHAR(255) NOT NULL,            -- Хэш пароля
                       role VARCHAR(255) NOT NULL, -- ENUM('admin', 'manager', 'employee') Роль пользователя
                       client_id INT,                                 -- Ссылка на клиента (если это клиент)
                       employee_id INT,                               -- Ссылка на сотрудника (если это сотрудник)
                       FOREIGN KEY (client_id) REFERENCES Clients(client_id),  -- Связь с таблицей Clients (если это клиент)
                       FOREIGN KEY (employee_id) REFERENCES Employees(employee_id)  -- Связь с таблицей Employees (если это сотрудник)
);
