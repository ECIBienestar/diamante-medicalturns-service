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

Astha Diagrama de
clases: [Astha Diagrama de clases](<assets/docs/Class-diagrams.asta>)

- [ ] Diagrama de Componentes

![Diagrama de Componentes](<assets/imgs/diagrams/components.jpg>)

- [ ] Diagrama de Secuencia

[Diagramas de secuencia](<assets/docs/Sequence-Diagrams.pdf>)

- [ ] Diagrama de Datos

![Diagrama de Datos](<assets/imgs/diagrams/data.png>)

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

- Evidencia de cobertura:

Como parte del aseguramiento de la calidad del software, se realizaron pruebas automatizadas enfocadas en validar el correcto funcionamiento de los componentes desarrollados. Para medir el alcance de dichas pruebas, se utilizó JaCoCo (Java Code Coverage), una herramienta que permite analizar qué porcentaje del código fuente ha sido ejecutado durante la ejecución de las pruebas.

Además, se integró SonarQube para proporcionar un análisis más completo del estado del código, incluyendo métricas de cobertura, deuda técnica, duplicación de código y cumplimiento de buenas prácticas. Gracias a estas herramientas, se logró mantener una cobertura de pruebas superior al 90%, lo cual indica un alto nivel de validación automatizada sobre el código del sistema.

Este nivel de cobertura no solo refleja un esfuerzo por garantizar la confiabilidad del software, sino que también facilita el mantenimiento futuro del proyecto al reducir la probabilidad de errores en funcionalidades ya implementadas.

- Evidencia de cobertura JaCoCo:

![alt text](assets/imgs/test/jacoco.jpg) 

- Evidencia de cobertura SonarQube:

![alt text](assets/imgs/test/sonar.jpg) 

- Las pruebas están ubicadas en:  
  `src/test/java/eci/cvds/ecibeneficio/diamante_medicalturns_service`

- Tecnologías utilizadas:
    - **JUnit 5**
    - **Mockito**
    - **Spring Boot Test**
    - **SonarQube**

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
