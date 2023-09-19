<p float="left">
  <img src="/visuales_github/image1.jpeg" width="200" />
  <img src="" width="30" />
  <img src="/visuales_github/image2.jpeg" width="200" /> 
  <img src="" width="30" />
  <img src="/visuales_github/image3.jpeg" width="200" />
</p>

# RetoYape
Mini proyecto para evaluación técnica para la empresa Yape.

El proyecto esta estructurado con un clean architecture con ausencia de capa dominio por la simplicidad de la lógica de negocio.
Google propone que la cada de dominio es opcional, aquí no la usamos porque no tenemos muchos casos de uso.

Se implemento Dagger Hilt para inyectar dependencias, puntualmente las vistas, retrofit, repositorio y viewmodel.

Usamos corrutinas para operaciones en segundo plano, y livedata para escuchar cambios del viewmodel en la vista, tambien podriamos haberlo hecho con flows, que permite captar los cambios de la fuente de datos en los colectores.

Para las vistas usamos xml, pero otra opcion era hacerlo con jetpack compose.

Para pruebas unitarias trabajamos con junit y mockeamos objetos con Mockk.

Gracias.


  
  

