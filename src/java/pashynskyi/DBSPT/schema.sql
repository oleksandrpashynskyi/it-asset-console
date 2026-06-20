DROP DATABASE IF EXISTS itassetdb;
CREATE DATABASE itassetdb;
USE itassetdb;

CREATE TABLE Techs (
    TechID VARCHAR(20) NOT NULL,
    FName VARCHAR(50),
    LName VARCHAR(50),
    FullName VARCHAR(100) GENERATED ALWAYS AS (CONCAT(FName, ' ', LName)) VIRTUAL,
    TechRole ENUM('Manager', 'Service Tech', 'Help Desk'),
    CONSTRAINT pk_Techs_TechID PRIMARY KEY (TechID)
);

CREATE TABLE TechLogin (
    TechID VARCHAR(20) NOT NULL,
    Username VARCHAR(50),
    Password VARCHAR(50),
    CONSTRAINT pk_TechLogin_TechID PRIMARY KEY (TechID),
    CONSTRAINT fk_TechLogin_Techs FOREIGN KEY (TechID) REFERENCES Techs(TechID)
);

CREATE TABLE Users (
    UserID VARCHAR(20) NOT NULL,
    FName VARCHAR(50),
    LName VARCHAR(50),
    FullName VARCHAR(100) GENERATED ALWAYS AS (CONCAT(FName, ' ', LName)) VIRTUAL,
    UserRole ENUM('Staff', 'Faculty'),
    CONSTRAINT pk_Users_UserID PRIMARY KEY (UserID)
);

CREATE TABLE UserLogin (
    UserID VARCHAR(20) NOT NULL,
    Username VARCHAR(50),
    Password VARCHAR(50),
    CONSTRAINT pk_UserLogin_UserID PRIMARY KEY (UserID),
    CONSTRAINT fk_UserLogin_Users FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

CREATE TABLE Workstations (
    CmptrID VARCHAR(20) NOT NULL,
    CmptrOS VARCHAR(20),
    CmptrLocation INT,
    CmptrStatus ENUM('Online', 'Offline'),
    AssignedUser VARCHAR(20),
    AssignedTech VARCHAR(20),
    CONSTRAINT pk_Workstations_CmptrID PRIMARY KEY (CmptrID),
    CONSTRAINT fk_Workstations_Users FOREIGN KEY (AssignedUser) REFERENCES Users(UserID),
    CONSTRAINT fk_Workstations_Techs FOREIGN KEY (AssignedTech) REFERENCES Techs(TechID)
);