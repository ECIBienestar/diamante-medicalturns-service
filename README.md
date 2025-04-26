# 📅 Módulo de Gestión de Turnos para Servicios de Bienestar

## 📌 Descripción del Módulo

Este módulo permite a los miembros de la comunidad universitaria (estudiantes, docentes, administrativos y personal de servicios generales) gestionar y visualizar turnos para atención en los servicios de bienestar institucional: medicina general, odontología y psicología.  
El sistema contempla la asignación de turnos desde tablets de autoservicio, control administrativo por parte del personal autorizado y seguimiento por parte de los profesionales de la salud.

---

## 🛠️ Tecnologías Utilizadas

- **Java 17**
- **Spring Boot**
- **PostgreSQL**
- **Apache Kafka**
- **Spring Cloud Bus**
- **JWT (JSON Web Token)**
- **Lombok**
- **Maven**

---

## 🔧 Funcionamiento del Módulo

### 🔗 Interacción con Otros Módulos

El módulo opera como un microservicio independiente, orquestado dentro de una arquitectura basada en microservicios y expuesto mediante un **API Gateway** que gestiona la autenticación y el enrutamiento de peticiones.

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

| Servicio             | Descripción                                               |
|----------------------|-----------------------------------------------------------|
| **Auth Service**     | Autenticación y emisión de tokens JWT                    |
| **API Gateway**      | Enrutamiento y control de acceso                          |
| **Users Service**    | Consulta y validación de usuarios                         |
| **Estadistics Service** | Registro histórico y generación de reportes              |
| **Event Bus**        | Middleware de eventos asincrónicos (Kafka + Cloud Bus)    |

#### 🔗 Diagrama de microservicios
![microservicios](assets/imgs/microservicios.png)

### 🏗️ Estilo Arquitectónico

> Aún por definir completamente.

### ⚙️ Funcionamiento Interno

El **MOD-LLL-001: Módulo de Turnos Médicos** expone una API RESTful para gestionar la creación, consulta y modificación de turnos. Utiliza autenticación basada en JWT y eventos distribuidos para la comunicación entre servicios. Incluye integración con tablets para asignación física de turnos y módulos visuales para pantallas de atención.

> 🔍 *Más detalles disponibles en el documento de análisis de requerimientos.*

[Análisis Requerimientos](<assets/docs/Análisis Requerimientos.pdf>)

---

## 📊 Diagramas del Sistema

- [ ] Diagrama de Clases
- [ ] Diagrama de Componentes
- [ ] Diagrama de Secuencia
- [ ] Diagrama de Datos

---

## 🚀 Funcionalidades del Módulo

### 📡 Endpoints REST

| Método | Endpoint       | Descripción                     | Entrada                | Salida              |
|--------|----------------|---------------------------------|------------------------|---------------------|
| GET    | `/turns`       | Lista turnos disponibles        | N/A                    | Lista de turnos     |
| POST   | `/turns`       | Crear nuevo turno               | Objeto JSON del turno  | Turno creado        |
| PUT    | `/turns/{id}`  | Modificar turno existente       | ID + datos actualizados | Turno actualizado   |
| DELETE | `/turns/{id}`  | Eliminar turno por ID           | ID del turno           | Confirmación        |

### 😊 Happy Path

| Escenario                      | Resultado esperado                             |
|-------------------------------|-----------------------------------------------|
| Crear turno correctamente      | Se registra el turno y se devuelve confirmación |
| Obtener lista de turnos       | Se devuelve lista de turnos disponibles        |
| Modificar turno existente      | Se actualiza con éxito y se refleja el cambio  |

### 🚨 Manejo de Errores

| Código | Motivo                     | Respuesta esperada            |
|--------|----------------------------|-------------------------------|
| 400    | Datos inválidos            | Error de validación detallado |
| 404    | Turno no encontrado        | Mensaje de recurso ausente    |
| 500    | Error interno del servidor | Stacktrace y detalles en logs |

---

## 📬 Uso de Colas de Mensajería

| Tópico Kafka      | Evento Disparado          | Resultado Esperado             | Happy Path                        | Dead Letter Queue (DLQ)        |
|-------------------|---------------------------|--------------------------------|------------------------------------|--------------------------------|
| `turns.created`   | Turno creado               | Evento consumido correctamente | El turno se registra y se notifica | Si hay error, mensaje reintenta y luego DLQ |
| `turns.updated`   | Turno modificado           | Evento sincronizado            | El cambio se refleja en otros sistemas | DLQ tras múltiples fallos     |

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
git clone <URL-del-repositorio>
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

### ☁️ Usando el despliegue en Azure

Puedes consumir el API ya desplegado accediendo a su documentación en línea:

- **Swagger en Azure:**
```
https://<tu_proyecto>.azurewebsites.net/swagger-ui.html
```

Este endpoint se encuentra protegido por autenticación JWT, por lo que deberás obtener un token desde el **Auth Service** antes de realizar peticiones.

---

### 🌐 Ejemplo de consumo desde un Frontend en React

A continuación, se muestra un ejemplo básico de cómo consumir el endpoint `GET /turns` desde una aplicación React utilizando `fetch` y un token JWT:

---

### 📁 `api.ts` – Configuración base del cliente HTTP

```ts
// src/api.ts
export const API_URL = 'https://<tu_proyecto>.azurewebsites.net';

export const fetchWithToken = async (endpoint: string, token: string) => {
  const res = await fetch(`${API_URL}${endpoint}`, {
    method: 'GET',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    }
  });

  if (!res.ok) {
    const errorText = await res.text();
    throw new Error(`Error ${res.status}: ${errorText}`);
  }

  return res.json();
};
```

---

### 📁 `services.ts` – Servicios específicos para turnos

```ts
// src/services.ts
import { fetchWithToken } from './api';

export const getTurnos = (token: string) => {
  return fetchWithToken('/turns', token);
};
```

---

### 📁 Componente React

```tsx
// src/components/Turnos.tsx
import { useEffect, useState } from 'react';
import { getTurnos } from '../services';

function Turnos() {
  const [turnos, setTurnos] = useState([]);
  const [error, setError] = useState<string | null>(null);

  const token = '<TOKEN_JWT_AQUI>'; // O idealmente, desde context/localStorage

  useEffect(() => {
    getTurnos(token)
      .then(setTurnos)
      .catch(err => setError(err.message));
  }, []);

  return (
    <div>
      <h2>Turnos</h2>
      {error ? <p>{error}</p> : (
        <ul>
          {turnos.map((t: any) => (
            <li key={t.id}>{t.fecha} - {t.servicio}</li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default Turnos;
```

---

## 🚀 Evidencia de CI/CD y Despliegue

- El proyecto se encuentra desplegado en Azure.
- Acceso a la API mediante Swagger:  
  👉 [Ver en Azure](https://<tu_proyecto>.azurewebsites.net/swagger-ui.html)
- Pipelines configurados:
  - GitHub Actions para pruebas y builds
  - Azure Pipelines para despliegue automático

---

## 🗂️ Estructura del Proyecto

```
C:.
├───.mvn
│   └───wrapper
└───src
    ├───main
    │   ├───java
    │   │   └───eci
    │   │       └───cvds
    │   │           └───ecibeneficio
    │   │               └───diamante_medicalturns_service
    │   │                   ├───controller
    │   │                   ├───model
    │   │                   ├───repository
    │   │                   └───service
    │   └───resources
    └───test
        └───java
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