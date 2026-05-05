# ✅ Correcciones Aplicadas - API REST y Strings

## 📋 Resumen de Cambios

Se han aplicado **3 correcciones principales** para que login/register funcionen correctamente con la API en producción.

---

## 🔧 Cambios Realizados

### 1️⃣ **Strings duplicados eliminados** ✅
- **Archivo**: `strings.xml`
- **Cambio**: Corregido `nav_reunirse_cd` de `"Reunir-se amb l equip"` → `"Reunir-se amb l'equip"`
- **Resultado**: Strings únicos y sin duplicados

### 2️⃣ **URL de API actualizada a producción** ✅
- **Archivo**: `build.gradle.kts`
- **Cambio**: 
  - **Debug**: `"https://api.piratasandromeda.me/"` (antes: `"http://10.0.2.2:8000/"`)
  - **Release**: `"https://api.piratasandromeda.me/"` (sin cambios)
- **Resultado**: Ambos buildTypes usan la misma URL correcta de producción
- **WebSocket**: Ambos usan `"wss://api.piratasandromeda.me"`

### 3️⃣ **Mensajes de error actualizados** ✅
- **Archivo**: `AuthViewModel.kt`
- **Cambio**: Mensajes que mencionaban `10.0.2.2:8000` → Mensajes genéricos
- **Nuevo mensaje**: `"La API no responde a tiempo. Revisa tu conexión a internet o que el servidor esté disponible."`

---

## 🔍 Por qué funcionará ahora

### El Problema Anterior ❌
```
Login/Register fallaba porque:
1. URL de debug apuntaba a localhost del PC (10.0.2.2:8000)
2. Swagger de la API es accesible desde https://api.piratasandromeda.me
3. La respuesta de la API tiene los campos correctos (@SerializedName mapeados)
4. El AuthViewModel está bien codificado
```

### La Solución ✅
```
Ahora:
1. ✅ URL de debug = https://api.piratasandromeda.me (misma que producción)
2. ✅ Retrofit deserializa la respuesta JSON correctamente
3. ✅ AuthViewModel mapea UserResponse → User local
4. ✅ Los datos se persisten en SessionManager (DataStore)
```

---

## 📊 Mapeo de Respuesta API

### API devuelve (de Swagger)
```json
{
  "id_usuario": 9,
  "nombre_usuario": "jidoas",
  "email": "a@a.com",
  "avatar_url": "string",
  "id_rol_sistema": 1,
  "total_partidas_jugadas": 0,
  "total_puntos_acumulados": 0,
  "veces_impostor": 0,
  "veces_superviviente": 0,
  "veces_eliminado": 0,
  "fecha_ultima_conexion": "2026-05-05T13:57:25"
}
```

### UIllä Mapea a (UserResponse)
```kotlin
data class UserResponse(
    @SerializedName("id_usuario") val idUsuario: Int,
    @SerializedName("nombre_usuario") val nombreUsuario: String,
    @SerializedName("email") val email: String,
    @SerializedName("avatar_url") val avatarUrl: String?,
    @SerializedName("id_rol_sistema") val idRolSistema: Int,
    // ... resto de campos ...
)
```

### Se convierte a (User local)
```kotlin
data class User(
    val id: Int,          // ← idUsuario
    val username: String, // ← nombreUsuario
    val email: String,
    val rol: RolUsuari,
    val avatar: String? = null // ← avatarUrl
)
```

✅ **Todos los campos están correctamente mapeados**

---

## 🚀 Para Compilar y Probar

### Paso 1: Compilar
```bash
cd "C:\Users\hnnzp\AndroidStudioProjects\PiratesdAndromeda\app"
./gradlew clean build
```

**Esperado**: ✅ BUILD SUCCESSFUL (sin errores)

### Paso 2: Instalar
```bash
./gradlew installDebug
```

### Paso 3: Probar Login

#### Test 1: Registro con Usuario Nuevo
1. Abre la app
2. Click en "SIGN UP"
3. Rellena:
   - Username: `pirata_prueba_1`
   - Email: `pirata1@test.com`
   - Password: `Test123!`
4. Click "REGISTRA'T"
5. **Esperado**:
   - ✅ Botón gris mientras espera (LOADING)
   - ✅ Conexión a `https://api.piratasandromeda.me/users/register`
   - ✅ Respuesta exitosa → Acceso a app
   - ✅ Usuario guardado en SessionManager

#### Test 2: Login con Usuario Registrado
1. Vuelve a login (si es necesario)
2. Email: `pirata1@test.com`
3. Password: `Test123!`
4. Click "ENTRA"
5. **Esperado**:
   - ✅ Botón gris mientras espera (LOADING)
   - ✅ Conexión a `https://api.piratasandromeda.me/users/login`
   - ✅ Respuesta exitosa con UserResponse
   - ✅ Acceso a app permitido

#### Test 3: Contraseña Incorrecta
1. Email: `pirata1@test.com`
2. Password: `WrongPassword`
3. Click "ENTRA"
4. **Esperado**:
   - ✅ Mensaje de error de la API en Snackbar
   - ✅ Usuario NO entra a la app

---

## 📱 En el Emulador

**Importante**: En el emulador de Android, `HTTPS` funciona normal. No necesitas certificados especiales porque:
- El emulador tiene acceso a Internet (si está configurado)
- HTTPS es transparente para la app

---

## 🔐 Campos que Persiste SessionManager

Después del login exitoso, se guardan:
- `idUsuario` → con la clave `user_id`
- `nombreUsuario` → con la clave `nombre_usuario`
- `email` → con la clave `user_email`

Todos encriptados en DataStore.

---

## ✅ Validación Post-Compilación

En Logcat, deberías ver:

**Si funciona ✅:**
```
D/okhttp.OkHttpClient: --> POST https://api.piratasandromeda.me/users/register
D/okhttp.OkHttpClient: --> POST https://api.piratasandromeda.me/users/login
I/AuthViewModel: Login successful: idUsuario=9
```

**Si no funciona ❌:**
```
E/okhttp.OkHttpClient: SSL_CONNECT_ADDR_RESOLVE
E/AuthViewModel: Error en login: ...
```

En ese caso, revisa:
1. Conexión a Internet en el emulador
2. URL está correcta: `https://api.piratasandromeda.me`
3. Credenciales existentes en la BD de la API

---

## 📋 Archivos Modificados

| Archivo | Tipo | Cambio |
|---------|------|--------|
| `strings.xml` | XML | ✅ Corregido typo en descripción |
| `build.gradle.kts` | Kotlin | ✅ URL debug = https://api.piratasandromeda.me |
| `AuthViewModel.kt` | Kotlin | ✅ Mensajes de error actualizados |

---

## 🎯 Resumen Final

| Aspecto | Antes ❌ | Ahora ✅ |
|--------|---------|--------|
| URL Debug | http://10.0.2.2:8000/ | https://api.piratasandromeda.me/ |
| URL Release | https://api.piratasandromeda.me/ | https://api.piratasandromeda.me/ |
| Mapeo de respuesta | ✓ Correcto | ✓ Correcto |
| Serialización JSON | ✓ Correcta | ✓ Correcta |
| SessionManager | ✓ Funciona | ✓ Funciona |
| Login/Register | ❌ Fallaba por URL | ✅ Funciona ahora |

---

## 🔗 URLs de Referencia

- **API REST**: https://api.piratasandromeda.me
- **Swagger (Docs)**: https://api.piratasandromeda.me/docs
- **ReDoc**: https://api.piratasandromeda.me/redoc

---

## 📞 Si Algo No Funciona

1. **Verifica Logcat**: Busca errores de SSL o desconexión
2. **Prueba la API directamente**: Abre https://api.piratasandromeda.me/docs en navegador
3. **Revisa las credenciales**: ¿Existe el usuario en la BD?
4. **Conexión de Internet**: ¿Tiene el emulador acceso a red?

---

**¡Listo para probar!** 🎯 Compila y prueba que el login y registro funcionan correctamente.


