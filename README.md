# UniLab

UniLab es una aplicacion Android educativa pensada para estudiantes universitarios de ingenieria y ciencias, con enfoque inicial en Ingenieria Electronica. La app funciona completamente **offline** y combina calculadoras tecnicas, conversores de unidades y herramientas de apoyo para el estudio.

## Objetivo

La primera version de UniLab busca ofrecer una base funcional, clara y escalable para resolver ejercicios tecnicos y mostrar el procedimiento de calculo de forma didactica.

## Funcionalidades implementadas

### Home
- Pantalla principal con acceso a:
  - **Electronica**
  - **Conversores**
  - **Herramientas**

### Electronica
- **Ley de Ohm**
  - Calculo de voltaje, corriente y resistencia
  - Validacion de entradas
  - Resultado con unidades
  - Procedimiento paso a paso
- **Resistencias en serie**
  - Soporte para multiples resistencias
  - Unidades: `Ω`, `kΩ`, `MΩ`
  - Procedimiento de suma
- **Resistencias en paralelo**
  - Soporte para dos o mas resistencias
  - Formula especial para dos resistencias
  - Procedimiento detallado

### Conversores
- Conversor reutilizable para:
  - Voltaje
  - Corriente
  - Resistencia
  - Frecuencia
  - Potencia

### Herramientas
- Tabla de **prefijos SI**
- Panel de **conversion rapida**

## Tecnologias utilizadas

- **Kotlin**
- **Jetpack Compose**
- **Material 3**
- Arquitectura ligera separando:
  - UI
  - logica de negocio
  - modelos
  - utilidades

## Versiones actuales del proyecto

- **Android Gradle Plugin:** `9.3.0`
- **Kotlin:** `2.2.10`
- **Compose BOM:** `2026.02.01`
- **Compose UI resuelto:** `1.10.4`
- **Material 3 resuelto:** `1.4.0`
- **compileSdk / targetSdk:** `36`
- **minSdk:** `24`

## Estructura principal

```text
app/src/main/java/com/gaspersoft/unilab/
|- domain/
|  |- calculator/
|  |- model/
|- ui/
|  |- components/
|  |- navigation/
|  |- screens/
|  |  |- home/
|  |  |- electronics/
|  |  |- converters/
|  |  |- tools/
|  |- theme/
|- utils/
```

## Validaciones incluidas

- Campos vacios
- Numeros invalidos
- Division por cero
- Resistencias en paralelo iguales a cero
- Valores negativos cuando no corresponden
- Formateo legible de resultados decimales

## Pruebas

Se incluyen pruebas unitarias para:
- Ley de Ohm
- Resistencias en serie
- Resistencias en paralelo
- Casos invalidos relevantes

## Como ejecutar

### Desde Android Studio
1. Abre el proyecto.
2. Espera la sincronizacion de Gradle.
3. Ejecuta la app en un emulador o dispositivo Android.

### Desde terminal en Windows

```powershell
.\gradlew.bat assembleDebug
```

## Como ejecutar pruebas

```powershell
.\gradlew.bat testDebugUnitTest
```

## Como generar un Android App Bundle

```powershell
.\gradlew.bat bundleRelease
```

Salida esperada:

```text
app\build\outputs\bundle\release\app-release.aab
```

## Publicacion futura

El proyecto ya esta preparado como base para continuar hacia Google Play:
- `applicationId`: `com.gaspersoft.unilab`
- `versionCode`: `1`
- `versionName`: `1.0`
- Sin backend
- Sin base de datos
- Sin analytics
- Sin permisos innecesarios

## Estado actual

UniLab ya cuenta con una primera version funcional, compilable y lista para seguir creciendo con nuevas herramientas y calculadoras.
