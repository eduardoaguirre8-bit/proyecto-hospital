/* Validaciones y utilidades de la pantalla de login */
function validarLogin() {
  var usuario = document.getElementById('usuario').value.trim();
  var clave = document.getElementById('clave').value.trim();
  if (usuario === '') { aviso('Ingresa tu usuario'); return false; }
  if (clave === '') { aviso('Ingresa tu clave'); return false; }
  return true;
}
function aviso(texto) {
  if (typeof Swal !== 'undefined') {
    Swal.fire({ icon: 'warning', title: 'Faltan datos', text: texto, confirmButtonColor: '#0e8a8a' });
  } else { alert(texto); }
}
(function () {
  var btn = document.getElementById('verClave'), input = document.getElementById('clave');
  if (!btn || !input) return;
  btn.addEventListener('click', function () {
    var visible = input.type === 'text';
    input.type = visible ? 'password' : 'text';
    btn.innerHTML = '<i class="bi ' + (visible ? 'bi-eye' : 'bi-eye-slash') + '"></i>';
  });
})();
