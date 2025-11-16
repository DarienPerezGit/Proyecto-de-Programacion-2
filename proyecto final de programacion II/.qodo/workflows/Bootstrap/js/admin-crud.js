// ===== FUNCIONES CRUD PARA ADMINISTRADORES =====
// admin-crud.js

// ===== CRUD PAÍSES =====
async function cargarTablaPaises() {
    console.log('🌍 Cargando tabla de países desde backend...');
    
    const tbody = document.getElementById('paisesTableBody');
    
    if (!tbody) {
        console.log('❌ No se encontró la tabla de países');
        return;
    }
    
    try {
        // Intentar obtener países del backend
        const paises = await apiService.obtenerPaises();
        console.log('✅ Países obtenidos del backend:', paises.length);
        
        // Guardar en localStorage como caché
        localStorage.setItem('paises', JSON.stringify(paises));
        
        await renderizarTablaPaises(paises);
        
    } catch (error) {
        console.error('❌ Error al cargar países desde backend:', error);
        console.log('🔄 Usando localStorage como fallback...');
        
        // Fallback a localStorage
        const paises = storageService.obtenerPaises();
        await renderizarTablaPaises(paises);
    }
}

async function renderizarTablaPaises(paises) {
    const tbody = document.getElementById('paisesTableBody');
    
    if (paises.length === 0) {
        tbody.innerHTML = `<tr><td colspan="6" class="text-center text-muted py-4">No hay países registrados</td></tr>`;
        return;
    }
    
    tbody.innerHTML = paises.map(pais => {
        // Calcular empresas en este país
        const empresas = storageService.obtenerEmpresasExtended();
        const empresasEnPais = empresas.filter(emp => emp.pais === pais.nombre).length;
        
        return `
            <tr>
                <td>
                    <div class="d-flex align-items-center">
                        <div class="item-icon empresa me-3">
                            <i class="fas fa-flag"></i>
                        </div>
                        <div>
                            <strong>${pais.nombre}</strong>
                            <br><small class="text-muted">Capital: ${pais.capital}</small>
                        </div>
                    </div>
                </td>
                <td>${pais.capital}</td>
                <td>$${(pais.pib / 1000).toFixed(1)}B</td>
                <td>${((pais.numeroHabitantes || pais.habitantes) / 1000000).toFixed(1)}M</td>
                <td>${empresasEnPais}</td>
                <td>
                    <div class="d-flex gap-1">
                        <button class="btn btn-sm btn-outline-primary" onclick="editarPais(${pais.id})">
                            <i class="fas fa-edit"></i>
                        </button>
                        <button class="btn btn-sm btn-outline-danger" onclick="eliminarPais(${pais.id})">
                            <i class="fas fa-trash"></i>
                        </button>
                    </div>
                </td>
            </tr>
        `;
    }).join('');
    
    console.log('✅ Tabla de países renderizada:', paises.length, 'registros');
}

function nuevoPais() {
    console.log('➕ Abriendo modal de nuevo país...');
    
    try {
        crearModalPaisEmergencia();
        
        document.getElementById('paisModalTitle').textContent = 'Nuevo País';
        document.getElementById('paisForm').reset();
        document.getElementById('paisId').value = '';
        
        const modalElement = document.getElementById('paisModal');
        if (modalElement) {
            const modal = new bootstrap.Modal(modalElement);
            modal.show();
        }
        
    } catch (error) {
        console.error('❌ Error en nuevoPais:', error);
        if (typeof notificationService !== 'undefined') {
            notificationService.add({
                type: 'error',
                title: 'Error',
                message: error.message || 'No se pudo abrir el formulario'
            });
        }
    }
}

function editarPais(id) {
    console.log('✏️ Editando país ID:', id);
    
    try {
        const pais = storageService.obtenerPorId('paises', id);
        if (!pais) {
            alert('País no encontrado');
            return;
        }
        
        crearModalPaisEmergencia();
        
        document.getElementById('paisModalTitle').textContent = 'Editar País';
        document.getElementById('paisForm').reset();
        document.getElementById('paisId').value = pais.id;
        document.getElementById('paisNombre').value = pais.nombre;
        document.getElementById('paisCapital').value = pais.capital;
        document.getElementById('paisPIB').value = pais.pib;
        document.getElementById('paisHabitantes').value = pais.habitantes;
        
        const modalElement = document.getElementById('paisModal');
        if (modalElement) {
            const modal = new bootstrap.Modal(modalElement);
            modal.show();
        }
    } catch (error) {
        console.error('❌ Error en editarPais:', error);
        if (typeof notificationService !== 'undefined') {
            notificationService.add({
                type: 'error',
                title: 'Error',
                message: 'No se pudo cargar el país para editar'
            });
        }
    }
}

async function guardarPais() {
    console.log('💾 Guardando país en backend...');
    
    try {
        const form = document.getElementById('paisForm');
        
        if (!form.checkValidity()) {
            form.reportValidity();
            return;
        }
        
        const paisData = {
            nombre: document.getElementById('paisNombre').value,
            capital: document.getElementById('paisCapital').value,
            pib: parseInt(document.getElementById('paisPIB').value),
            habitantes: parseInt(document.getElementById('paisHabitantes').value)
        };
        
        const paisId = document.getElementById('paisId').value;
        
        try {
            let resultado;
            
            if (paisId) {
                // Editar país existente
                resultado = await apiService.actualizarPais(parseInt(paisId), paisData);
                console.log('✅ País editado en backend:', paisData.nombre);
            } else {
                // Nuevo país
                resultado = await apiService.crearPais(paisData);
                console.log('✅ Nuevo país creado en backend:', paisData.nombre);
            }
            
            // Guardar también en localStorage como respaldo
            if (paisId) {
                storageService.actualizar('paises', parseInt(paisId), paisData);
            } else {
                storageService.crearPais(resultado);
            }
            
            // Mostrar notificación de éxito
            if (typeof notificationService !== 'undefined') {
                notificationService.add({
                    type: 'success',
                    title: 'País guardado',
                    message: `${paisData.nombre} fue guardado exitosamente en la base de datos`
                });
            }
            
        } catch (backendError) {
            console.error('❌ Error al guardar en backend:', backendError);
            console.log('🔄 Guardando en localStorage como fallback...');
            
            // Fallback a localStorage
            if (paisId) {
                storageService.actualizar('paises', parseInt(paisId), paisData);
                console.log('✅ País editado localmente:', paisData.nombre);
            } else {
                storageService.crearPais(paisData);
                console.log('✅ Nuevo país creado localmente:', paisData.nombre);
            }
            
            if (typeof notificationService !== 'undefined') {
                notificationService.add({
                    type: 'warning',
                    title: 'País guardado localmente',
                    message: 'No se pudo conectar con el servidor, datos guardados en navegador'
                });
            }
        }
        
        // Cerrar modal
        const modalElement = document.getElementById('paisModal');
        if (modalElement) {
            const modal = bootstrap.Modal.getInstance(modalElement);
            if (modal) modal.hide();
        }
        
        // Recargar tabla
        await cargarTablaPaises();
        
    } catch (error) {
        console.error('❌ Error en guardarPais:', error);
        if (typeof notificationService !== 'undefined') {
            notificationService.add({
                type: 'error',
                title: 'Error al guardar',
                message: error.message || 'No se pudo guardar el país'
            });
        }
    }
}

function eliminarPais(id) {
    try {
        const pais = storageService.obtenerTodos('paises').find(p => p.id === id);
        
        if (!pais) {
            showToast('País no encontrado', 'danger');
            return;
        }
        
        // Verificar si estamos en modo backend
        const modoBackend = sessionStorage.getItem('modoBackend') === 'true';
        
        if (modoBackend && typeof eliminarPaisConGestion === 'function') {
            // Usar nueva función de gestión avanzada
            eliminarPaisConGestion(id, pais.nombre);
            return;
        }
        
        // Modo localStorage (lógica original)
        const empresas = storageService.obtenerEmpresasExtended();
        const empresasEnPais = empresas.filter(emp => emp.pais === id);
        
        if (empresasEnPais.length > 0) {
            // Hay empresas, preguntar qué hacer
            mostrarModalEliminarPaisConEmpresas(pais, empresasEnPais);
        } else {
            // No hay empresas, eliminar directamente
            showConfirm(
                `¿Está seguro de que desea eliminar el país "${pais.nombre}"?`,
                function() {
                    if (storageService.eliminar('paises', id)) {
                        cargarTablaPaises();
                        showToast('El país ha sido eliminado correctamente', 'success');
                    } else {
                        showToast('Error al eliminar el país', 'danger');
                    }
                },
                {
                    title: 'Eliminar País',
                    icon: 'fas fa-trash text-danger',
                    confirmText: 'Eliminar',
                    btnClass: 'btn-danger'
                }
            );
        }
    } catch (error) {
        console.error('Error en eliminarPais:', error);
        showToast('Error al eliminar el país', 'danger');
    }
}

function mostrarModalEliminarPaisConEmpresas(pais, empresasEnPais) {
    const todosPaises = storageService.obtenerTodos('paises').filter(p => p.id !== pais.id);
    
    const modalHtml = `
        <div class="modal fade" id="eliminarPaisModal" tabindex="-1">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header bg-danger text-white">
                        <h5 class="modal-title">
                            <i class="fas fa-exclamation-triangle me-2"></i>
                            Eliminar País con Empresas
                        </h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <div class="alert alert-warning">
                            <i class="fas fa-building me-2"></i>
                            El país <strong>"${pais.nombre}"</strong> tiene <strong>${empresasEnPais.length}</strong> empresa(s) registrada(s).
                        </div>
                        
                        <h6 class="mb-3">¿Qué desea hacer con las empresas?</h6>
                        
                        <div class="form-check mb-3">
                            <input class="form-check-input" type="radio" name="accionEmpresas" id="eliminarEmpresas" value="eliminar" checked>
                            <label class="form-check-label" for="eliminarEmpresas">
                                <strong>Eliminar todas las empresas</strong>
                                <br><small class="text-muted">Las empresas y sus empleados serán eliminados/desactivados</small>
                            </label>
                        </div>
                        
                        <div class="form-check">
                            <input class="form-check-input" type="radio" name="accionEmpresas" id="reubicarEmpresas" value="reubicar" ${todosPaises.length === 0 ? 'disabled' : ''}>
                            <label class="form-check-label" for="reubicarEmpresas">
                                <strong>Reubicar en otro país</strong>
                                <br><small class="text-muted">Transferir empresas a otro país existente</small>
                            </label>
                        </div>
                        
                        <div id="selectorPaisDestino" class="mt-3" style="display: none;">
                            <label class="form-label">País destino:</label>
                            <select class="form-select" id="paisDestinoSelect">
                                <option value="">Seleccionar país...</option>
                                ${todosPaises.map(p => `<option value="${p.id}">${p.nombre}</option>`).join('')}
                            </select>
                        </div>
                        
                        ${todosPaises.length === 0 ? '<div class="alert alert-info mt-3"><i class="fas fa-info-circle me-2"></i>No hay otros países disponibles para reubicar</div>' : ''}
                        
                        <div class="mt-3">
                            <h6>Empresas afectadas:</h6>
                            <ul class="list-group">
                                ${empresasEnPais.map(e => `<li class="list-group-item">${e.nombre}</li>`).join('')}
                            </ul>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                        <button type="button" class="btn btn-danger" id="btnConfirmarEliminarPais">
                            <i class="fas fa-trash me-2"></i>Eliminar País
                        </button>
                    </div>
                </div>
            </div>
        </div>
    `;
    
    // Remover modal anterior si existe
    const modalAnterior = document.getElementById('eliminarPaisModal');
    if (modalAnterior) {
        modalAnterior.remove();
    }
    
    // Agregar nuevo modal
    document.body.insertAdjacentHTML('beforeend', modalHtml);
    
    // Configurar eventos
    const radioEliminar = document.getElementById('eliminarEmpresas');
    const radioReubicar = document.getElementById('reubicarEmpresas');
    const selectorDestino = document.getElementById('selectorPaisDestino');
    const btnConfirmar = document.getElementById('btnConfirmarEliminarPais');
    
    if (radioReubicar) {
        radioReubicar.addEventListener('change', () => {
            selectorDestino.style.display = radioReubicar.checked ? 'block' : 'none';
        });
    }
    
    if (radioEliminar) {
        radioEliminar.addEventListener('change', () => {
            selectorDestino.style.display = 'none';
        });
    }
    
    btnConfirmar.addEventListener('click', () => {
        const accion = document.querySelector('input[name="accionEmpresas"]:checked').value;
        
        if (accion === 'reubicar') {
            const paisDestinoId = document.getElementById('paisDestinoSelect').value;
            if (!paisDestinoId) {
                showToast('Debe seleccionar un país destino', 'warning');
                return;
            }
            ejecutarEliminacionPais(pais.id, 'reubicar', paisDestinoId);
        } else {
            ejecutarEliminacionPais(pais.id, 'eliminar');
        }
    });
    
    // Mostrar modal
    const modal = new bootstrap.Modal(document.getElementById('eliminarPaisModal'));
    modal.show();
}

function ejecutarEliminacionPais(paisId, accion, paisDestinoId = null) {
    const empresas = storageService.obtenerEmpresasExtended();
    const empresasEnPais = empresas.filter(emp => emp.pais === paisId);
    
    if (accion === 'eliminar') {
        // Eliminar todas las empresas del país
        empresasEnPais.forEach(empresa => {
            // Usar la función existente que maneja empleados
            if (typeof ejecutarEliminacionEmpresa === 'function') {
                ejecutarEliminacionEmpresa(empresa.id, 'desactivar');
            } else {
                // Fallback: eliminar directamente
                storageService.eliminar('empresas', empresa.id);
            }
        });
        
    } else if (accion === 'reubicar') {
        // Reubicar empresas a otro país
        const todasEmpresas = storageService.obtenerEmpresasExtended();
        const paisDestino = storageService.obtenerTodos('paises').find(p => p.id === paisDestinoId);
        
        if (paisDestino) {
            const empresasActualizadas = todasEmpresas.map(e => {
                if (e.pais === paisId) {
                    return {...e, pais: paisDestinoId};
                }
                return e;
            });
            
            localStorage.setItem('empresas', JSON.stringify(empresasActualizadas));
        }
    }
    
    // Eliminar país
    storageService.eliminar('paises', paisId);
    
    // Cerrar modal
    const modal = bootstrap.Modal.getInstance(document.getElementById('eliminarPaisModal'));
    if (modal) modal.hide();
    
    // Actualizar interfaz
    cargarTablaPaises();
    if (typeof cargarTablaEmpresas === 'function') {
        cargarTablaEmpresas();
    }
    
    const mensaje = accion === 'eliminar' 
        ? 'País eliminado y empresas eliminadas' 
        : 'País eliminado y empresas reubicadas';
    showToast(mensaje, 'success');
}

// ===== CRUD ASESORES =====
async function cargarTablaAsesores() {
    console.log('🎓 Cargando tabla de asesores desde backend...');
    
    const tbody = document.getElementById('asesoresTableBody');
    
    if (!tbody) {
        console.log('❌ No se encontró la tabla de asesores');
        return;
    }
    
    try {
        // Intentar obtener asesores del backend
        const asesores = await apiService.obtenerAsesores();
        console.log('✅ Asesores obtenidos del backend:', asesores.length);
        
        // Guardar en localStorage como caché
        localStorage.setItem('asesores', JSON.stringify(asesores));
        
        await renderizarTablaAsesores(asesores);
        
    } catch (error) {
        console.error('❌ Error al cargar asesores desde backend:', error);
        console.log('🔄 Usando localStorage como fallback...');
        
        // Fallback a localStorage
        const asesores = storageService.obtenerAsesores();
        await renderizarTablaAsesores(asesores);
    }
}

async function renderizarTablaAsesores(asesores) {
    const tbody = document.getElementById('asesoresTableBody');
    const empresas = storageService.obtenerEmpresasExtended();
    
    console.log('📊 Asesores a renderizar:', asesores.length);
    
    if (asesores.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="7" class="text-center text-muted py-4">
                    <i class="fas fa-user-tie fa-3x mb-3 d-block" style="opacity: 0.3"></i>
                    <p>No hay asesores registrados</p>
                    <button class="btn btn-success btn-sm mt-2" onclick="nuevoAsesor()">
                        <i class="fas fa-plus me-2"></i>Agregar Primer Asesor
                    </button>
                </td>
            </tr>`;
        return;
    }
    
    tbody.innerHTML = asesores.map(asesor => {
        // Obtener áreas de expertise del asesor (puede ser array o string separado por comas)
        let areasDisplay = 'Sin áreas asignadas';
        if (asesor.areasExpertise) {
            const areas = Array.isArray(asesor.areasExpertise) 
                ? asesor.areasExpertise 
                : asesor.areasExpertise.split(',').map(a => a.trim());
            areasDisplay = areas.length > 0 
                ? `<span class="badge bg-secondary me-1">${areas[0]}</span>${areas.length > 1 ? ` +${areas.length - 1}` : ''}`
                : 'Sin áreas asignadas';
        }
        
        // Obtener empresas asignadas al asesor
        let empresasDisplay = 'Sin empresas asignadas';
        if (asesor.empresaIds && Array.isArray(asesor.empresaIds) && asesor.empresaIds.length > 0) {
            const empresasNombres = asesor.empresaIds
                .map(id => {
                    const emp = empresas.find(e => e.id === id);
                    return emp ? emp.nombre : null;
                })
                .filter(n => n !== null);
            
            if (empresasNombres.length > 0) {
                empresasDisplay = `<span class="badge bg-primary me-1">${empresasNombres[0]}</span>${empresasNombres.length > 1 ? ` +${empresasNombres.length - 1}` : ''}`;
            }
        }
        
        return `
            <tr>
                <td>
                    <div class="d-flex align-items-center">
                        <div class="item-icon asesor me-2">
                            <i class="fas fa-user-tie"></i>
                        </div>
                        <strong>${asesor.codigo}</strong>
                    </div>
                </td>
                <td>
                    <strong>${asesor.nombre}</strong>
                    <br><small class="text-muted">${asesor.direccion || 'Sin dirección'}</small>
                </td>
                <td>
                    <span class="badge bg-info">${asesor.titulacion}</span>
                </td>
                <td>${areasDisplay}</td>
                <td>${empresasDisplay}</td>
                <td>
                    <span class="badge bg-success">${asesor.estado || 'Activo'}</span>
                </td>
                <td>
                    <div class="d-flex gap-1">
                        <button class="btn btn-sm btn-outline-primary" onclick="editarAsesor('${asesor.codigo}')" title="Editar">
                            <i class="fas fa-edit"></i>
                        </button>
                        <button class="btn btn-sm btn-outline-info" onclick="verDetalleAsesor('${asesor.codigo}')" title="Ver detalle">
                            <i class="fas fa-eye"></i>
                        </button>
                        <button class="btn btn-sm btn-outline-danger" onclick="eliminarAsesor('${asesor.codigo}')" title="Eliminar">
                            <i class="fas fa-trash"></i>
                        </button>
                    </div>
                </td>
            </tr>
        `;
    }).join('');
    
    console.log('✅ Tabla de asesores cargada:', asesores.length, 'registros');
}

function nuevoAsesor() {
    console.log('➕ Abriendo modal de nuevo asesor...');
    
    try {
        crearModalAsesorEmergencia();
        
        document.getElementById('asesorModalTitle').textContent = 'Nuevo Asesor';
        document.getElementById('asesorForm').reset();
        document.getElementById('asesorCodigo').value = '';
        
        // Limpiar selecciones de áreas y empresas
        const areasSelect = document.getElementById('asesorAreas');
        const empresasSelect = document.getElementById('asesorEmpresas');
        
        if (areasSelect) {
            Array.from(areasSelect.options).forEach(option => option.selected = false);
        }
        
        if (empresasSelect) {
            Array.from(empresasSelect.options).forEach(option => option.selected = false);
        }
        
        const modalElement = document.getElementById('asesorModal');
        if (modalElement) {
            const modal = new bootstrap.Modal(modalElement);
            modal.show();
        }
        
    } catch (error) {
        console.error('❌ Error en nuevoAsesor:', error);
        showToast('Error al abrir modal: ' + error.message, 'danger');
    }
}

function editarAsesor(codigo) {
    console.log('✏️ Editando asesor:', codigo);
    
    try {
        const asesor = storageService.obtenerPorId('asesores', codigo);
        if (!asesor) {
            showToast('Asesor no encontrado', 'danger');
            return;
        }
        
        crearModalAsesorEmergencia();
        
        document.getElementById('asesorModalTitle').textContent = 'Editar Asesor';
        document.getElementById('asesorForm').reset();
        document.getElementById('asesorCodigo').value = asesor.codigo;
        document.getElementById('asesorNombre').value = asesor.nombre;
        document.getElementById('asesorDireccion').value = asesor.direccion || '';
        document.getElementById('asesorTitulacion').value = asesor.titulacion;
        
        // Cargar áreas de expertise en select múltiple
        const areasSelect = document.getElementById('asesorAreas');
        if (areasSelect) {
            // Limpiar selección previa
            Array.from(areasSelect.options).forEach(option => option.selected = false);
            
            // Seleccionar áreas del asesor
            if (asesor.areasExpertise && Array.isArray(asesor.areasExpertise)) {
                asesor.areasExpertise.forEach(area => {
                    Array.from(areasSelect.options).forEach(option => {
                        if (option.value === area) {
                            option.selected = true;
                        }
                    });
                });
            }
        }
        
        // Cargar empresas asignadas
        const empresasSelect = document.getElementById('asesorEmpresas');
        if (empresasSelect && asesor.empresaIds && Array.isArray(asesor.empresaIds)) {
            Array.from(empresasSelect.options).forEach(option => {
                option.selected = asesor.empresaIds.includes(parseInt(option.value));
            });
        }
        
        const modalElement = document.getElementById('asesorModal');
        if (modalElement) {
            const modal = new bootstrap.Modal(modalElement);
            modal.show();
        }
        
    } catch (error) {
        console.error('❌ Error en editarAsesor:', error);
        showToast('Error al editar asesor: ' + error.message, 'danger');
    }
}

async function guardarAsesor() {
    console.log('💾 Guardando asesor en backend...');
    
    try {
        const form = document.getElementById('asesorForm');
        
        if (!form.checkValidity()) {
            form.reportValidity();
            return;
        }
        
        // Obtener áreas seleccionadas del select múltiple
        const areasSelect = document.getElementById('asesorAreas');
        const areasSeleccionadas = Array.from(areasSelect.selectedOptions).map(option => option.value);
        
        if (areasSeleccionadas.length === 0) {
            showToast('Debe seleccionar al menos un área de expertise', 'warning');
            return;
        }
        
        // Obtener empresas seleccionadas
        const empresasSelect = document.getElementById('asesorEmpresas');
        const empresasSeleccionadas = Array.from(empresasSelect.selectedOptions).map(option => parseInt(option.value));
        
        const asesorData = {
            nombre: document.getElementById('asesorNombre').value,
            direccion: document.getElementById('asesorDireccion').value,
            email: document.getElementById('asesorEmail').value,
            telefono: document.getElementById('asesorTelefono').value,
            titulacion: document.getElementById('asesorTitulacion').value,
            areasExpertise: areasSeleccionadas,
            empresaIds: empresasSeleccionadas,
            estado: 'Activo'
        };
        
        const asesorCodigo = document.getElementById('asesorCodigo').value;
        
        try {
            let resultado;
            
            if (asesorCodigo) {
                // Editar asesor existente - buscar ID real
                const asesor = storageService.obtenerPorId('asesores', asesorCodigo);
                if (asesor && asesor.id) {
                    resultado = await apiService.actualizarAsesor(asesor.id, asesorData);
                    console.log('✅ Asesor editado en backend:', asesorData.nombre);
                    showToast('Asesor actualizado en base de datos', 'success');
                } else {
                    throw new Error('No se encontró el ID del asesor');
                }
            } else {
                // Nuevo asesor
                resultado = await apiService.crearAsesor(asesorData);
                console.log('✅ Nuevo asesor creado en backend:', asesorData.nombre);
                showToast('Asesor creado en base de datos', 'success');
            }
            
            // Guardar también en localStorage como respaldo
            if (asesorCodigo) {
                storageService.actualizar('asesores', asesorCodigo, resultado);
            } else {
                storageService.crearAsesor(resultado);
            }
            
        } catch (backendError) {
            console.error('❌ Error al guardar en backend:', backendError);
            console.log('🔄 Guardando en localStorage como fallback...');
            
            // Fallback a localStorage
            if (asesorCodigo) {
                storageService.actualizar('asesores', asesorCodigo, asesorData);
                showToast('Asesor actualizado localmente', 'warning');
                console.log('✅ Asesor editado localmente:', asesorData.nombre);
            } else {
                storageService.crearAsesor(asesorData);
                showToast('Asesor creado localmente (sin conexión)', 'warning');
                console.log('✅ Nuevo asesor creado localmente:', asesorData.nombre);
            }
        }
        
        // Cerrar modal
        const modalElement = document.getElementById('asesorModal');
        if (modalElement) {
            const modal = bootstrap.Modal.getInstance(modalElement);
            if (modal) modal.hide();
        }
        
        // Recargar tabla
        await cargarTablaAsesores();
        
    } catch (error) {
        console.error('❌ Error en guardarAsesor:', error);
        showToast('Error al guardar asesor: ' + error.message, 'danger');
    }
}

function verDetalleAsesor(codigo) {
    const asesor = storageService.obtenerPorId('asesores', codigo);
    if (!asesor) {
        showToast('Asesor no encontrado', 'danger');
        return;
    }
    
    const asesorias = obtenerAsesoriasPorAsesor(codigo);
    const empresas = storageService.obtenerEmpresasExtended();
    
    // Obtener nombres de empresas asignadas
    let empresasHtml = '<span class="text-muted">Sin empresas asignadas</span>';
    if (asesor.empresaIds && Array.isArray(asesor.empresaIds) && asesor.empresaIds.length > 0) {
        const empresasNombres = asesor.empresaIds
            .map(id => {
                const emp = empresas.find(e => e.id === id);
                return emp ? `<span class="badge bg-primary me-1">${emp.nombre}</span>` : null;
            })
            .filter(n => n !== null)
            .join('');
        empresasHtml = empresasNombres || empresasHtml;
    }
    
    // Obtener áreas de expertise
    let areasHtml = '<span class="text-muted">Sin áreas asignadas</span>';
    if (asesor.areasExpertise && Array.isArray(asesor.areasExpertise) && asesor.areasExpertise.length > 0) {
        areasHtml = asesor.areasExpertise
            .map(area => `<span class="badge bg-secondary me-1">${area}</span>`)
            .join('');
    }
    
    const modalHtml = `
        <div class="modal fade" id="detalleAsesorModal" tabindex="-1">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header bg-primary text-white">
                        <h5 class="modal-title">
                            <i class="fas fa-user-tie me-2"></i>
                            Detalle del Asesor
                        </h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <div class="row g-3">
                            <div class="col-md-6">
                                <label class="text-muted small">Código</label>
                                <p class="mb-0"><strong>${asesor.codigo}</strong></p>
                            </div>
                            <div class="col-md-6">
                                <label class="text-muted small">Estado</label>
                                <p class="mb-0">
                                    <span class="badge bg-${asesor.estado === 'Activo' ? 'success' : 'secondary'}">
                                        ${asesor.estado || 'Activo'}
                                    </span>
                                </p>
                            </div>
                            <div class="col-12">
                                <label class="text-muted small">Nombre Completo</label>
                                <p class="mb-0"><strong>${asesor.nombre}</strong></p>
                            </div>
                            <div class="col-12">
                                <label class="text-muted small">Dirección</label>
                                <p class="mb-0">${asesor.direccion || '<span class="text-muted">No especificada</span>'}</p>
                            </div>
                            <div class="col-md-6">
                                <label class="text-muted small">Email</label>
                                <p class="mb-0">${asesor.email || '<span class="text-muted">No especificado</span>'}</p>
                            </div>
                            <div class="col-md-6">
                                <label class="text-muted small">Teléfono</label>
                                <p class="mb-0">${asesor.telefono || '<span class="text-muted">No especificado</span>'}</p>
                            </div>
                            <div class="col-12">
                                <label class="text-muted small">Titulación</label>
                                <p class="mb-0"><span class="badge bg-info">${asesor.titulacion}</span></p>
                            </div>
                            <div class="col-12">
                                <label class="text-muted small">Áreas de Expertise</label>
                                <p class="mb-0">${areasHtml}</p>
                            </div>
                            <div class="col-12">
                                <label class="text-muted small">Empresas Asignadas</label>
                                <p class="mb-0">${empresasHtml}</p>
                            </div>
                            <div class="col-md-6">
                                <label class="text-muted small">Total Asesorías</label>
                                <p class="mb-0"><strong class="text-primary">${asesorias.length}</strong></p>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cerrar</button>
                        <button type="button" class="btn btn-primary" onclick="editarAsesor('${asesor.codigo}'); bootstrap.Modal.getInstance(document.getElementById('detalleAsesorModal')).hide();">
                            <i class="fas fa-edit me-2"></i>Editar
                        </button>
                    </div>
                </div>
            </div>
        </div>
    `;
    
    // Remover modal anterior si existe
    const modalAnterior = document.getElementById('detalleAsesorModal');
    if (modalAnterior) {
        modalAnterior.remove();
    }
    
    // Agregar nuevo modal
    document.body.insertAdjacentHTML('beforeend', modalHtml);
    
    // Mostrar modal
    const modal = new bootstrap.Modal(document.getElementById('detalleAsesorModal'));
    modal.show();
}

function eliminarAsesor(codigo) {
    if (!confirm('¿Estás seguro de que quieres eliminar este asesor?\n\nEsta acción no se puede deshacer.')) {
        return;
    }
    
    try {
        // Verificar si tiene asesorías activas
        const asesorias = obtenerAsesoriasPorAsesor(codigo);
        if (asesorias.length > 0) {
            alert(`❌ No se puede eliminar el asesor porque tiene ${asesorias.length} asesoría(s) activa(s)`);
            return;
        }
        
        if (storageService.eliminar('asesores', codigo)) {
            cargarTablaAsesores();
            alert('✅ Asesor eliminado correctamente');
        } else {
            alert('❌ Error al eliminar el asesor');
        }
    } catch (error) {
        console.error('❌ Error en eliminarAsesor:', error);
        alert('Error al eliminar asesor: ' + error.message);
    }
}

// ===== REPORTES Y ESTADÍSTICAS =====
function cargarReportes() {
    console.log('📊 Cargando reportes...');
    
    try {
        const stats = storageService.obtenerEstadisticas();
        
        // Actualizar estadísticas principales
        actualizarElementoReporte('reporte-empresas', stats.totalEmpresas);
        actualizarElementoReporte('reporte-vendedores', stats.totalVendedores);
        actualizarElementoReporte('reporte-asesores', stats.totalAsesores);
        actualizarElementoReporte('reporte-facturacion', `$${(stats.facturacionTotal / 1000000).toFixed(1)}M`);
        
        // Cargar gráficos
        cargarGraficoEmpresasPorPais(stats.empresasPorPais);
        cargarGraficoVendedoresPorEmpresa(stats.vendedoresPorEmpresa);
        cargarReporteDetallado(stats);
        
    } catch (error) {
        console.error('❌ Error en cargarReportes:', error);
    }
}

function cargarGraficoEmpresasPorPais(datos) {
    const container = document.getElementById('chartEmpresasPais');
    if (!container) return;
    
    if (datos.length === 0) {
        container.innerHTML = '<p class="text-muted">No hay datos para mostrar</p>';
        return;
    }
    
    // Simular gráfico con HTML (podrías integrar Chart.js después)
    container.innerHTML = `
        <div class="list-group">
            ${datos.map(item => `
                <div class="list-item">
                    <div class="item-content">
                        <h6>${item.pais}</h6>
                        <p>${item.cantidad} empresa(s)</p>
                    </div>
                    <div class="progress" style="width: 100px; height: 10px;">
                        <div class="progress-bar" style="width: ${(item.cantidad / Math.max(...datos.map(d => d.cantidad))) * 100}%"></div>
                    </div>
                </div>
            `).join('')}
        </div>
    `;
}

function cargarGraficoVendedoresPorEmpresa(datos) {
    const container = document.getElementById('chartVendedoresEmpresa');
    if (!container) return;
    
    if (datos.length === 0) {
        container.innerHTML = '<p class="text-muted">No hay datos para mostrar</p>';
        return;
    }
    
    container.innerHTML = `
        <div class="list-group">
            ${datos.map(item => `
                <div class="list-item">
                    <div class="item-content">
                        <h6>${item.empresa}</h6>
                        <p>${item.cantidad} vendedor(es)</p>
                    </div>
                    <span class="badge bg-primary">${item.cantidad}</span>
                </div>
            `).join('')}
        </div>
    `;
}

function cargarReporteDetallado(stats) {
    const container = document.getElementById('reporteDetallado');
    if (!container) return;
    
    container.innerHTML = `
        <div class="col-md-6">
            <div class="content-card">
                <div class="card-header">
                    <h6><i class="fas fa-chart-pie me-2"></i>Resumen General</h6>
                </div>
                <div class="card-body">
                    <div class="info-item">
                        <label>Total Empresas:</label>
                        <span>${stats.totalEmpresas}</span>
                    </div>
                    <div class="info-item">
                        <label>Total Vendedores:</label>
                        <span>${stats.totalVendedores}</span>
                    </div>
                    <div class="info-item">
                        <label>Total Asesores:</label>
                        <span>${stats.totalAsesores}</span>
                    </div>
                    <div class="info-item">
                        <label>Facturación Total:</label>
                        <span>$${(stats.facturacionTotal / 1000000).toFixed(1)}M</span>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="content-card">
                <div class="card-header">
                    <h6><i class="fas fa-trending-up me-2"></i>Métricas Clave</h6>
                </div>
                <div class="card-body">
                    <div class="info-item">
                        <label>Promedio Vendedores/Empresa:</label>
                        <span>${stats.totalEmpresas > 0 ? (stats.totalVendedores / stats.totalEmpresas).toFixed(1) : 0}</span>
                    </div>
                    <div class="info-item">
                        <label>Facturación Promedio:</label>
                        <span>$${stats.totalEmpresas > 0 ? ((stats.facturacionTotal / stats.totalEmpresas) / 1000000).toFixed(1) : 0}M</span>
                    </div>
                    <div class="info-item">
                        <label>Países con Operaciones:</label>
                        <span>${stats.empresasPorPais.filter(p => p.cantidad > 0).length}</span>
                    </div>
                </div>
            </div>
        </div>
    `;
}

function exportarReporte() {
    console.log('📤 Exportando reporte completo a PDF...');
    
    try {
        // Obtener estadísticas actualizadas
        const stats = storageService.obtenerEstadisticas();
        
        // Verificar que pdfExportService esté disponible
        if (typeof pdfExportService === 'undefined') {
            alert('⚠️ El servicio de exportación PDF no está disponible. Por favor, recarga la página.');
            return;
        }
        
        // Preparar datos para el PDF
        const reportData = {
            title: 'Reporte Completo del Sistema',
            subtitle: `Generado el ${new Date().toLocaleDateString('es-AR')} a las ${new Date().toLocaleTimeString('es-AR')}`,
            sections: [
                {
                    title: 'Resumen General',
                    content: [
                        { label: 'Total Empresas', value: stats.totalEmpresas },
                        { label: 'Total Vendedores', value: stats.totalVendedores },
                        { label: 'Total Asesores', value: stats.totalAsesores },
                        { label: 'Total Países', value: stats.totalPaises },
                        { label: 'Total Captaciones', value: stats.totalCaptaciones },
                        { label: 'Facturación Total', value: `$${(stats.facturacionTotal / 1000000).toFixed(2)}M` }
                    ]
                },
                {
                    title: 'Métricas Clave',
                    content: [
                        { label: 'Promedio Vendedores/Empresa', value: stats.totalEmpresas > 0 ? (stats.totalVendedores / stats.totalEmpresas).toFixed(2) : '0' },
                        { label: 'Promedio Asesores/Empresa', value: stats.totalEmpresas > 0 ? (stats.totalAsesores / stats.totalEmpresas).toFixed(2) : '0' },
                        { label: 'Facturación Promedio/Empresa', value: stats.totalEmpresas > 0 ? `$${((stats.facturacionTotal / stats.totalEmpresas) / 1000000).toFixed(2)}M` : '$0M' },
                        { label: 'Países con Operaciones', value: stats.empresasPorPais.filter(p => p.cantidad > 0).length }
                    ]
                },
                {
                    title: 'Empresas por País',
                    content: stats.empresasPorPais.map(item => ({
                        label: item.pais,
                        value: `${item.cantidad} empresa(s)`
                    }))
                },
                {
                    title: 'Vendedores por Empresa',
                    content: stats.vendedoresPorEmpresa.slice(0, 10).map(item => ({
                        label: item.empresa,
                        value: `${item.cantidad} vendedor(es)`
                    }))
                }
            ]
        };
        
        // Llamar al servicio de exportación
        pdfExportService.exportarReporteCompleto(reportData);
        
        console.log('✅ Reporte exportado exitosamente');
        
    } catch (error) {
        console.error('❌ Error al exportar reporte:', error);
        alert('❌ Error al exportar el reporte: ' + error.message);
    }
}

// ===== FUNCIONES AUXILIARES =====
function actualizarElementoReporte(id, valor) {
    const elemento = document.getElementById(id);
    if (elemento) {
        elemento.textContent = valor;
    }
}

// ===== MODALES DE EMERGENCIA (PARA ADMIN) =====
function crearModalPaisEmergencia() {
    if (document.getElementById('paisModal')) {
        console.log('✅ Modal de país ya existe');
        return;
    }
    
    console.log('🔄 Creando modal de país de emergencia...');
    
    const modalHTML = `
    <div class="modal fade" id="paisModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content cyber-modal">
                <div class="modal-header">
                    <h5 class="modal-title" id="paisModalTitle">Nuevo País</h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <form id="paisForm">
                        <input type="hidden" id="paisId">
                        <div class="row g-3">
                            <div class="col-12">
                                <label for="paisNombre" class="form-label">Nombre del País *</label>
                                <input type="text" class="form-control" id="paisNombre" required>
                            </div>
                            <div class="col-12">
                                <label for="paisCapital" class="form-label">Capital *</label>
                                <input type="text" class="form-control" id="paisCapital" required>
                            </div>
                            <div class="col-md-6">
                                <label for="paisPIB" class="form-label">PIB (millones USD) *</label>
                                <input type="number" class="form-control" id="paisPIB" required>
                            </div>
                            <div class="col-md-6">
                                <label for="paisHabitantes" class="form-label">Habitantes *</label>
                                <input type="number" class="form-control" id="paisHabitantes" required>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                    <button type="button" class="btn btn-primary" onclick="guardarPais()">Guardar País</button>
                </div>
            </div>
        </div>
    </div>
    `;
    
    document.body.insertAdjacentHTML('beforeend', modalHTML);
    console.log('✅ Modal de país creado (emergencia)');
}

function crearModalAsesorEmergencia() {
    if (document.getElementById('asesorModal')) {
        console.log('✅ Modal de asesor ya existe');
        return;
    }
    
    console.log('🔄 Creando modal de asesor de emergencia...');
    
    // Obtener empresas para el selector
    const empresas = storageService.obtenerEmpresasExtended();
    const empresasOptions = empresas.map(e => `<option value="${e.id}">${e.nombre}</option>`).join('');
    
    const modalHTML = `
    <div class="modal fade" id="asesorModal" tabindex="-1">
        <div class="modal-dialog modal-lg">
            <div class="modal-content cyber-modal">
                <div class="modal-header">
                    <h5 class="modal-title" id="asesorModalTitle">Nuevo Asesor</h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <form id="asesorForm">
                        <input type="hidden" id="asesorCodigo">
                        <div class="row g-3">
                            <div class="col-md-6">
                                <label for="asesorNombre" class="form-label">Nombre *</label>
                                <input type="text" class="form-control" id="asesorNombre" required>
                            </div>
                            <div class="col-md-6">
                                <label for="asesorDireccion" class="form-label">Dirección</label>
                                <input type="text" class="form-control" id="asesorDireccion">
                            </div>
                            <div class="col-md-6">
                                <label for="asesorEmail" class="form-label">Email</label>
                                <input type="email" class="form-control" id="asesorEmail">
                            </div>
                            <div class="col-md-6">
                                <label for="asesorTelefono" class="form-label">Teléfono</label>
                                <input type="tel" class="form-control" id="asesorTelefono">
                            </div>
                            <div class="col-12">
                                <label for="asesorTitulacion" class="form-label">Titulación *</label>
                                <input type="text" class="form-control" id="asesorTitulacion" 
                                       placeholder="Ej: PhD en Telecomunicaciones" required>
                            </div>
                            <div class="col-12">
                                <label for="asesorAreas" class="form-label">Áreas de Expertise *</label>
                                <select class="form-select" id="asesorAreas" multiple size="5" required>
                                    <option value="Telecomunicaciones">Telecomunicaciones</option>
                                    <option value="Entretenimiento">Entretenimiento</option>
                                    <option value="Streaming">Streaming</option>
                                    <option value="Tecnología">Tecnología</option>
                                    <option value="Medios">Medios</option>
                                    <option value="5G">5G</option>
                                    <option value="Fibra Óptica">Fibra Óptica</option>
                                    <option value="Seguridad">Seguridad</option>
                                    <option value="Cloud">Cloud Computing</option>
                                    <option value="IoT">Internet de las Cosas (IoT)</option>
                                </select>
                                <small class="form-text text-muted">
                                    Mantén presionado Ctrl (Windows) o Cmd (Mac) para seleccionar múltiples áreas
                                </small>
                            </div>
                            <div class="col-12">
                                <label for="asesorEmpresas" class="form-label">Empresas Asignadas</label>
                                <select class="form-select" id="asesorEmpresas" multiple size="4">
                                    ${empresasOptions}
                                </select>
                                <small class="form-text text-muted">
                                    Selecciona las empresas a las que asesora
                                </small>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                    <button type="button" class="btn btn-primary" onclick="guardarAsesor()">Guardar Asesor</button>
                </div>
            </div>
        </div>
    </div>
    `;
    
    document.body.insertAdjacentHTML('beforeend', modalHTML);
    console.log('✅ Modal de asesor creado (emergencia)');
}

// ===== EXPORTAR FUNCIONES ADMIN =====
window.cargarTablaPaises = cargarTablaPaises;
window.nuevoPais = nuevoPais;
window.editarPais = editarPais;
window.guardarPais = guardarPais;
window.eliminarPais = eliminarPais;

window.cargarTablaAsesores = cargarTablaAsesores;
window.nuevoAsesor = nuevoAsesor;
window.guardarAsesor = guardarAsesor;
window.editarAsesor = editarAsesor;
window.verDetalleAsesor = verDetalleAsesor;
window.eliminarAsesor = eliminarAsesor;

window.cargarReportes = cargarReportes;
window.exportarReporte = exportarReporte;

window.crearModalPaisEmergencia = crearModalPaisEmergencia;
window.crearModalAsesorEmergencia = crearModalAsesorEmergencia;

console.log("🛠️ Funciones CRUD de Administrador cargadas correctamente");