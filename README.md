# UncleBobFarm - Sistema de Gestión de Granja

Un sistema de gestión integral para granjas que permite administrar animales, ubicaciones, empleados y actividades diarias.

## Características

- 🐄 Gestión de animales (vacas, caballos, gallinas)
- 🏠 Administración de ubicaciones (establos, gallineros, corrales)
- 👥 Control de empleados y roles
- 📋 Registro de actividades (limpieza, alimentación, salud)
- 📊 Seguimiento de inventario
- 🔄 Monitoreo de salud animal
- 🧹 Control de limpieza y mantenimiento

## Tecnologías Utilizadas

- Java 17
- Spring Boot 3.x
- Spring Data JPA
- PostgreSQL
- Bootstrap 5
- JavaScript

## Requisitos

- JDK 17 o superior
- Maven 3.6 o superior
- PostgreSQL 12 o superior

## Instalación

1. Clonar el repositorio:
```bash
git clone https://github.com/tu-usuario/UncleBobFarm.git
cd UncleBobFarm
```

2. Configurar la base de datos:
- Crear una base de datos PostgreSQL llamada `unclebobfarm`
- Configurar las credenciales en `application.properties`

3. Compilar el proyecto:
```bash
mvn clean install
```

4. Ejecutar la aplicación:
```bash
mvn spring-boot:run
```

La aplicación estará disponible en `http://localhost:8081`

## Estructura del Proyecto

```
src/
├── main/
│   ├── java/
│   │   └── com/rancho/gestorGranja/
│   │       ├── controller/    # Controladores REST
│   │       ├── service/       # Lógica de negocio
│   │       ├── repository/    # Acceso a datos
│   │       ├── entity/        # Entidades JPA
│   │       └── config/        # Configuraciones
│   └── resources/
│       ├── static/           # Archivos estáticos (HTML, CSS, JS)
│       └── application.properties
└── test/                     # Pruebas unitarias
```

## API Endpoints

### Animales
- `GET /api/animales` - Listar todos los animales
- `POST /api/animales` - Crear nuevo animal
- `PUT /api/animales/{id}` - Actualizar animal
- `DELETE /api/animales/{id}` - Eliminar animal

### Ubicaciones
- `GET /api/ubicaciones` - Listar todas las ubicaciones
- `POST /api/ubicaciones` - Crear nueva ubicación
- `PUT /api/ubicaciones/{id}` - Actualizar ubicación
- `DELETE /api/ubicaciones/{id}` - Eliminar ubicación

### Actividades
- `GET /api/actividades` - Listar todas las actividades
- `POST /api/actividades` - Registrar nueva actividad
- `GET /api/actividades/tipo/{tipo}` - Filtrar actividades por tipo

## Contribuir

1. Fork el proyecto
2. Crear una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abrir un Pull Request

## Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para más detalles.

## Contacto

Tu Nombre - [@tutwitter](https://twitter.com/tutwitter) - email@ejemplo.com

Link del Proyecto: [https://github.com/tu-usuario/UncleBobFarm](https://github.com/tu-usuario/UncleBobFarm) 