(function () {
  'use strict';

  /* ----- Menú lateral (móvil) ----- */
  var menuBtn = document.getElementById('menuBtn'), overlay = document.getElementById('overlay');
  function toggleMenu(open) { document.body.classList.toggle('menu-open', open); }
  if (menuBtn) menuBtn.addEventListener('click', function () { toggleMenu(!document.body.classList.contains('menu-open')); });
  if (overlay) overlay.addEventListener('click', function () { toggleMenu(false); });

  /* ----- Fecha en la barra superior ----- */
  var fecha = document.getElementById('fechaHoy');
  if (fecha) fecha.textContent = new Date().toLocaleDateString('es-PE', { weekday: 'long', day: 'numeric', month: 'long', year: 'numeric' });

  /* ----- Contador de caracteres ----- */
  document.querySelectorAll('textarea[data-contador]').forEach(function (t) {
    var out = document.getElementById(t.dataset.contador);
    function act() { if (out) out.textContent = t.value.length + ' / ' + t.maxLength; }
    t.addEventListener('input', act); act();
  });

  /* ----- Modal de gráficos: carga las imágenes al abrir (siempre datos actuales) ----- */
  var modalG = document.getElementById('modalGraficos');
  if (modalG) modalG.addEventListener('show.bs.modal', function () {
    var t = Date.now();
    modalG.querySelectorAll('img[data-src]').forEach(function (img) {
      img.src = img.dataset.src + (img.dataset.src.indexOf('?') === -1 ? '?' : '&') + 't=' + t;
    });
  });

  if (typeof Swal === 'undefined') return;

  function confirmar(opts, peligro) {
    return Swal.fire(Object.assign({
      icon: 'question', showCancelButton: true, reverseButtons: true, focusCancel: true,
      cancelButtonText: 'Cancelar', buttonsStyling: false,
      customClass: {
        confirmButton: 'btn ' + (peligro ? 'btn-danger' : 'btn-primary') + ' px-4 ms-2',
        cancelButton: 'btn btn-light px-4'
      }
    }, opts));
  }
  var toast = Swal.mixin({ toast: true, position: 'top-end', showConfirmButton: false, timer: 3500, timerProgressBar: true });

  /* ----- Mensajes de resultado (?msg=...) ----- */
  var msgs = {
    creado: ['success', 'Paciente registrado correctamente'],
    actualizado: ['success', 'Cambios guardados correctamente'],
    eliminado: ['success', 'Paciente eliminado correctamente'],
    invalido: ['warning', 'Revisa los datos: hay campos vacíos o demasiado largos'],
    error: ['error', 'No se pudo completar la operación. Revisa la conexión y los datos.']
  };
  var params = new URLSearchParams(location.search), m = msgs[params.get('msg')];
  if (m) {
    toast.fire({ icon: m[0], title: m[1] });
    params.delete('msg');
    history.replaceState(null, '', location.pathname + (params.toString() ? '?' + params : ''));
  }

  /* ----- Formularios con confirmación y validación ----- */
  document.querySelectorAll('form[data-confirm]').forEach(function (f) {
    f.addEventListener('submit', function (e) {
      e.preventDefault();
      f.querySelectorAll('input[type=text],textarea').forEach(function (i) { i.value = i.value.trim(); });
      if (!f.checkValidity()) {
        f.classList.add('was-validated');
        toast.fire({ icon: 'warning', title: 'Completa los campos obligatorios' });
        var inv = f.querySelector(':invalid'); if (inv) inv.focus();
        return;
      }
      confirmar({ title: f.dataset.confirm, text: f.dataset.text, confirmButtonText: f.dataset.ok })
        .then(function (r) { if (r.isConfirmed) f.submit(); });
    });
  });

  /* ----- Eliminar (formulario POST con confirmación) ----- */
  document.querySelectorAll('form[data-eliminar]').forEach(function (f) {
    f.addEventListener('submit', function (e) {
      e.preventDefault();
      confirmar({
        icon: 'warning', title: '¿Eliminar paciente?',
        text: (f.dataset.nombre || 'Este paciente') + ' se eliminará de forma permanente.',
        confirmButtonText: 'Sí, eliminar'
      }, true).then(function (r) { if (r.isConfirmed) f.submit(); });
    });
  });

  /* ----- Cerrar sesión ----- */
  document.querySelectorAll('a[data-logout]').forEach(function (a) {
    a.addEventListener('click', function (e) {
      e.preventDefault();
      confirmar({ title: '¿Cerrar sesión?', confirmButtonText: 'Sí, salir' }).then(function (r) { if (r.isConfirmed) location.href = a.href; });
    });
  });

  /* ----- Buscador + paginación del listado ----- */
  var q = document.getElementById('buscar');
  var filas = Array.prototype.slice.call(document.querySelectorAll('#tabla tbody tr[data-fila]'));
  if (!q || !document.getElementById('tabla')) return;
  var porPagina = 8, pagina = 1;
  var pag = document.getElementById('paginador'), info = document.getElementById('infoPagina');
  var vacio = document.getElementById('sinResultados'), total = document.getElementById('totalRegistros');

  function render() {
    var t = q.value.trim().toLowerCase();
    var visibles = filas.filter(function (tr) { return tr.textContent.toLowerCase().indexOf(t) !== -1; });
    var paginas = Math.max(1, Math.ceil(visibles.length / porPagina));
    if (pagina > paginas) pagina = paginas;
    var ini = (pagina - 1) * porPagina;
    filas.forEach(function (tr) { tr.hidden = true; });
    visibles.slice(ini, ini + porPagina).forEach(function (tr) { tr.hidden = false; });
    if (vacio) vacio.hidden = visibles.length !== 0;
    if (total) total.textContent = visibles.length;
    info.textContent = visibles.length ? 'Mostrando ' + (ini + 1) + '–' + Math.min(ini + porPagina, visibles.length) + ' de ' + visibles.length : '';
    pag.innerHTML = '';
    if (paginas <= 1) return;
    function boton(txt, p, dis, on) {
      var b = document.createElement('button');
      b.type = 'button'; b.innerHTML = txt; b.disabled = dis;
      if (on) b.className = 'on';
      b.addEventListener('click', function () { pagina = p; render(); });
      pag.appendChild(b);
    }
    boton('&lsaquo;', pagina - 1, pagina === 1);
    for (var i = 1; i <= paginas; i++) boton(i, i, false, i === pagina);
    boton('&rsaquo;', pagina + 1, pagina === paginas);
  }
  q.addEventListener('input', function () { pagina = 1; render(); });
  render();
})();
