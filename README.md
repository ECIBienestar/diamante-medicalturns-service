# 📅 Módulo de Gestión de Turnos para Servicios de Bienestar

## 📌 Descripción del Módulo

Este módulo permite a los miembros de la comunidad universitaria (estudiantes, docentes, administrativos y personal de
servicios generales) gestionar y visualizar turnos para atención en los servicios de bienestar institucional: medicina
general, odontología y psicología.  
El sistema contempla la asignación de turnos desde tablets de autoservicio, control administrativo por parte del
personal autorizado y seguimiento por parte de los profesionales de la salud.

---

## 🛠️ Tecnologías Utilizadas

- **Java 17**
- **Spring Boot**
- **PostgreSQL**
- **Spring Cloud Bus**
- **JWT (JSON Web Token)**
- **Lombok**
- **Maven**
- **Swagger**

---

## 🔧 Funcionamiento del Módulo

### 🔗 Interacción con Otros Módulos

El módulo opera como un microservicio independiente, orquestado dentro de una arquitectura basada en microservicios y
expuesto mediante un **API Gateway** que gestiona la autenticación y el enrutamiento de peticiones.

#### 🔄 Flujo General de Interacción:

1. **Cliente (Web/Móvil)**: Envía solicitudes para turnos médicos al **API Gateway**.
2. **API Gateway**:
    - Obtiene un token JWT desde el **Auth Service**.
    - Valida el token y enruta la petición al microservicio correspondiente.
3. **Medical Shifts Service**:
    - Verifica el usuario y sus roles a través del **Users Service**.
    - Procesa la solicitud, registra la información en su base de datos y emite eventos al **Bus de Eventos**.
4. **Estadistics Service**:
    - Consume los eventos generados para generar reportes históricos y estadísticas de atención.

#### 🧩 Servicios Relacionados

| Servicio                | Descripción                                            |
|-------------------------|--------------------------------------------------------|
| **Auth Service**        | Autenticación y emisión de tokens JWT                  |
| **API Gateway**         | Enrutamiento y control de acceso                       |
| **Users Service**       | Consulta y validación de usuarios                      |
| **Estadistics Service** | Registro histórico y generación de reportes            |
| **Event Bus**           | Middleware de eventos asincrónicos (Kafka + Cloud Bus) |

#### 🔗 Diagrama de Microservicios

![microservicios](<assets/imgs/diagrams/microservices.png>)

### 🏗️ Estilo Arquitectónico

[DOCUMENTO DE ARQUITECTURA BACKEND](<assets/docs/Backend-architecture.pdf>)

### ⚙️ Funcionamiento Interno

El **MOD-LLL-001: Módulo de Turnos Médicos** expone una API RESTful para gestionar la creación, consulta y modificación
de turnos. Utiliza autenticación basada en JWT y eventos distribuidos para la comunicación entre servicios. Incluye
integración con tablets para asignación física de turnos y módulos visuales para pantallas de atención.

> 🔍 _Más detalles disponibles en el documento de análisis de requerimientos._

[Análisis Requerimientos](<assets/docs/Requirements-analysis.pdf>)

---

## 📊 Diagramas del Sistema

- [ ] Diagrama de Clases

![Diagrama de Clases](<assets/imgs/diagrams/class.jpg>)

El diagrama de clases representa las principales entidades involucradas en la gestión de turnos en el contexto de
bienestar universitario.  
Cada clase encapsula atributos y relaciones específicas para reflejar el comportamiento y la estructura del sistema.

### Clases Principales

### `User`

- Representa a los usuarios que pueden solicitar turnos.
- **Atributos**: `id`, `name`, `role`
- **Relaciones**: Tiene una relación con la clase `Turn` como paciente.

### `Doctor`

- Representa a los profesionales encargados de atender turnos.
- **Atributos**: `userId`, `speciality`
- **Relaciones**: Se relaciona con un `User` y está asociado a una especialidad médica.

### `Turn`

- Contiene la información de los turnos asignados o solicitados.
- **Atributos**: `id`, `code`, `date`, `levelAttention`, `priority`, `speciality`, `status`, `dateAttention`
- **Asociaciones**:
    - Un `Doctor` que atiende el turno.
    - Un `User` que solicita el turno.

### `UniversityWelfare`

- Contiene configuraciones relacionadas con la disponibilidad de turnos.
- **Atributos**: `id`, `disableTurns`, `disableTurnsBySpeciality` (lista de especialidades deshabilitadas)

### `Multimedia`

- Gestiona contenido informativo relacionado al servicio.
- **Atributos**: `id`, `name`, `type`, `url`, `duration`

### `SpecialitySequence`

- Lleva el control de la numeración secuencial de turnos por especialidad.
- **Atributos**: `id`, `speciality`, `sequence`

Astha Diagrama de
clases: [Astha Diagrama de clases](<assets/docs/Class-diagrams.asta>)

- [ ] Diagrama de Componentes

![Diagrama de Componentes](<assets/imgs/diagrams/components.jpg>)

El siguiente diagrama de componentes permite evidenciar el flujo completo y la estructura funcional del sistema *
*MedicalTurns**, abarcando desde la interfaz de usuario hasta la integración con servicios externos.

### Componentes Principales

### `UniversityWelfareService`

- Se encarga de la gestión de turnos, incluyendo su creación y actualización.

### `ReportService`

- Recopila y envía los datos necesarios para el análisis estadístico.
- Esta información es procesada por el servicio externo `bismuto-statistics-service`, el cual genera las estadísticas
  cuando son solicitadas.

### `MultimediaService`

- Administra los elementos multimedia del módulo, como imágenes y videos.
- Se apoya en el `MultimediaController` para exponer estos recursos a través de la API.


- [ ] Diagrama de Secuencia

[Diagramas de secuencia](<assets/docs/Sequence-Diagrams.pdf>)

En esta sección se documentan los distintos **diagramas de secuencia** que describen la interacción entre componentes
del sistema a lo largo del tiempo, específicamente en los flujos clave definidos para cada módulo funcional. Estos
diagramas son fundamentales para visualizar cómo los distintos servicios, controladores y entidades colaboran para
cumplir con los casos de uso definidos.

### Organización de los Diagramas

Los diagramas de secuencia están organizados en carpetas bajo el directorio `sequence-diagrams`, de acuerdo con los
módulos funcionales y técnicos del sistema. La estructura sigue el patrón de **arquitectura de capas**:

- `controller/`: contiene los diagramas centrados en las interacciones a nivel de API y controladores HTTP.
- `service/`: contiene los diagramas que detallan la lógica de negocio y cómo los servicios internos del sistema
  gestionan los procesos.

Cada subcarpeta dentro de `controller` y `service` corresponde a un módulo específico del sistema:

- `multimedia-controller` y `multimedia-service`  
  Documentan los flujos relacionados con la carga, consulta y validación de archivos multimedia asociados a turnos
  médicos o usuarios.

- `report-controller`  
  Contiene diagramas que representan la comunicación entre el sistema principal y el módulo externo de estadísticas y
  reportes, así como la exposición de esos datos al usuario.

- `university-welfare-controller` y `university-welfare-service`  
  Representan las operaciones relacionadas con el bienestar universitario, incluyendo flujos de asistencia social o
  seguimiento de estudiantes.

- `turn-service`  
  Incluye los diagramas para la gestión de turnos médicos, como la asignación, finalización (por asistencia o no
  asistencia) y consulta de disponibilidad.

---

Estos diagramas permiten una comprensión clara del comportamiento del sistema en tiempo de ejecución y son una
herramienta útil tanto para desarrolladores como para analistas funcionales.

- [ ] Diagrama de Datos

![Diagrama de Datos](<assets/imgs/diagrams/data.jpg>)

El sistema utiliza una base de datos relacional (**PostgreSQL**) cuyo modelo representa las entidades clave involucradas
en la gestión de turnos dentro del contexto de bienestar universitario.

A continuación, se describen las tablas principales y su propósito:

### `app_user`

Representa a los usuarios del sistema (como estudiantes o personal administrativo).  
Aunque el sistema general cuenta con un módulo centralizado de autenticación, este microservicio almacena localmente la
tabla `app_user` para evitar una dependencia directa, garantizando la **autonomía y resiliencia** del servicio, en
conformidad con los principios de diseño de microservicios.

### `doctor`

Representa a los profesionales encargados de atender los turnos.  
Está asociado directamente a un registro en `app_user` y contiene información adicional como la **especialidad médica**.

### `turn`

Registra los turnos solicitados por los usuarios, incluyendo atributos como:

- Código
- Fecha
- Nivel de atención
- Prioridad
- Especialidad
- Estado del turno

Cada turno se asocia tanto a un usuario como a un doctor.

### `university_welfare`

Define la configuración general del servicio de bienestar universitario, incluyendo si los **turnos están habilitados o
deshabilitados**.

### `disable_turns_speciality`

Relaciona las especialidades con los servicios de bienestar que tienen **turnos deshabilitados**.  
Esta tabla permite representar múltiples especialidades deshabilitadas por instancia de bienestar, solventando la
limitación de las bases de datos relacionales respecto al almacenamiento de listas.

### `multimedia`

Almacena contenido informativo como **videos o imágenes** relacionados con el servicio. Incluye atributos como:

- Duración
- Nombre
- Tipo
- URL

### `speciality_sequence`

Lleva un control de **numeración secuencial por especialidad**, lo que permite asignar un número de turno ordenado por
tipo de atención médica.

---

## 🚀 Funcionalidades del Módulo

### 📡 Endpoints REST

Puedes consumir el API ya desplegado accediendo a su documentación en línea:

- **Swagger en Azure:**

```
https://diamante-medicalturns-develop-dvb8c2cqfbh4gwbg.canadacentral-01.azurewebsites.net/swagger-ui/index.html
```  

### 😊 Happy Path

| Escenario                               | Resultado esperado                                                  |
|-----------------------------------------|---------------------------------------------------------------------|
| Crear un nuevo turno                    | Se registra el turno y se devuelve el turno creado                  |
| Obtener lista de turnos disponibles     | Se devuelve una lista actualizada de turnos                         |
| Eliminar un turno existente             | Se elimina correctamente y se confirma la operación                 |
| Consultar turnos por fecha específica   | Se devuelve lista de turnos correspondientes a la fecha             |
| Consultar turnos por usuario específico | Se devuelve lista de turnos asignados al usuario                    |
| Consultar turnos por especialidad       | Se devuelve lista de turnos filtrados por especialidad              |
| Habilitar turnos                        | Se habilitan los turnos y se confirma la habilitación               |
| Deshabilitar turnos                     | Se deshabilitan los turnos y se confirma la acción                  |
| Obtener el último turno llamado         | Se devuelve el último turno que fue llamado                         |
| Obtener turnos pendientes               | Se devuelve la lista de turnos en estado pendiente                  |
| Subir nuevo archivo multimedia          | Se sube correctamente el archivo y se devuelve el multimedia creado |
| Listar todos los archivos multimedia    | Se devuelve lista de todos los elementos multimedia                 |
| Consultar último elemento multimedia    | Se devuelve el elemento multimedia más reciente                     |
| Consultar multimedia por ID             | Se devuelve el elemento multimedia correspondiente al ID            |
| Eliminar multimedia por ID              | Se elimina el elemento multimedia y se confirma la acción           |
| Deshabilitar turnos de una especialidad | Se deshabilitan turnos de la especialidad indicada                  |
| Habilitar turnos de una especialidad    | Se habilitan turnos de la especialidad indicada                     |

### 🚨 Manejo de Errores

| Código | Mensaje de error             | Causa probable                         |
|--------|------------------------------|----------------------------------------|
| 400    | "Datos de entrada inválidos" | Validaciones fallidas en el formulario |
| 401    | "Usuario no autenticado"     | Token inválido o ausente               |
| 404    | "Turnos no disponibles"      | Los turnos están deshabilitados        |
| 404    | "Especialidad no disponible" | Especialidad deshabilitada             |
| 500    | "Error interno del servidor" | Fallo inesperado                       |

---

## 📬 Uso de Colas de Mensajería

| Tópico Kafka | Evento Disparado | Resultado Esperado | Happy Path | Dead Letter Queue (DLQ) |
|--------------|------------------|--------------------|------------|-------------------------|
| x            | x                | x                  | x          | x                       |

---

## 🧪 Evidencia de Pruebas

- Las pruebas están ubicadas en:  
  `src/test/java/eci/cvds/ecibeneficio/diamante_medicalturns_service`

- Tecnologías utilizadas:
    - **JUnit 5**
    - **Mockito**
    - **Spring Boot Test**

### ▶️ Ejecutar pruebas:

```bash
mvn test
```

---

## ▶️ Instrucciones para Ejecutar el Proyecto

### 🚀 De forma local

1. **Clona el repositorio:**

```bash
git clone https://github.com/ECIBienestar/diamante-medicalturns-service.git
```

2. **Navega a la raíz del proyecto:**

```bash
cd diamante_medicalturns_service
```

3. **Ejecuta el servicio con Maven:**

```bash
./mvnw spring-boot:run
```

4. **Accede al Swagger local:**

```
http://localhost:8080/swagger-ui.html
```

---

## 🚀 Evidencia de CI/CD y Despliegue

- El proyecto se encuentra desplegado en Azure.  
  👉 [Despliegue para pruebas](diamante-medicalturns-develop-dvb8c2cqfbh4gwbg.canadacentral-01.azurewebsites.net)  
  👉 [Despliegue para produccion](diamante-medicalturns-dzdja4b4bfayaqdk.canadacentral-01.azurewebsites.net)
- Pipelines configurados:
    - GitHub Actions para pruebas y builds
    - Azure Pipelines para despliegue automático

---

## 🗂️ Estructura del Proyecto

```
C:.
├───.github
│   └───workflows
├───.mvn
│   └───wrapper
├───assets
│   ├───docs
│   └───imgs
├───src
│   ├───main
│   │   ├───java
│   │   │   └───eci
│   │   │       └───cvds
│   │   │           └───ecibeneficio
│   │   │               └───diamante_medicalturns_service
│   │   │                   ├───config
│   │   │                   │   └───initializer
│   │   │                   ├───controller
│   │   │                   ├───dto
│   │   │                   │   ├───request
│   │   │                   │   └───response
│   │   │                   ├───exception
│   │   │                   ├───factory
│   │   │                   │   └───impl
│   │   │                   ├───model
│   │   │                   │   └───enums
│   │   │                   ├───repository
│   │   │                   │   └───projection
│   │   │                   ├───service
│   │   │                   │   └───impl
│   │   │                   └───utils
│   │   │                       ├───enums
│   │   │                       └───mapper
│   │   └───resources
│   └───test
│       └───java
│           └───eci
│               └───cvds
│                   └───ecibeneficio
│                       └───diamante_medicalturns_service
└───target
    ├───classes
    │   └───eci
    │       └───cvds
    │           └───ecibeneficio
    │               └───diamante_medicalturns_service
    │                   ├───config
    │                   │   └───initializer
    │                   ├───controller
    │                   ├───dto
    │                   │   ├───request
    │                   │   └───response
    │                   ├───exception
    │                   ├───factory
    │                   │   └───impl
    │                   ├───model
    │                   │   └───enums
    │                   ├───repository
    │                   │   └───projection
    │                   ├───service
    │                   │   └───impl
    │                   └───utils
    │                       ├───enums
    │                       └───mapper
    ├───generated-sources
    │   └───annotations
    ├───generated-test-sources
    │   └───test-annotations
    ├───maven-status
    │   └───maven-compiler-plugin
    │       ├───compile
    │       │   └───default-compile
    │       └───testCompile
    │           └───default-testCompile
    ├───surefire-reports
    └───test-classes
        └───eci
            └───cvds
                └───ecibeneficio
                    └───diamante_medicalturns_service
```

---

## 📝 Documentación del Código

Todo el código fuente cuenta con documentación mediante JavaDoc:

- 📘 **Clases** documentadas con su propósito.
- 🔧 **Métodos** descritos con entradas, salidas y comportamiento.
- 🧩 **Propiedades** explicadas según su función dentro del modelo.

---

**📌 Nota:** Este README se actualizará conforme avance el desarrollo del proyecto.
