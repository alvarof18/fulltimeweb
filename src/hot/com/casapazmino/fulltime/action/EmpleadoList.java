package com.casapazmino.fulltime.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityQuery;
import org.richfaces.model.DataProvider;
import org.richfaces.model.ExtendedTableDataModel;
import org.richfaces.model.selection.SimpleSelection;

import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.comun.GestionPermisoVacacion;
import com.casapazmino.fulltime.model.Departamento;
import com.casapazmino.fulltime.model.Empleado;
import com.casapazmino.fulltime.seguridad.model.UsuarioCiudad;

@Name("empleadoList")
public class EmpleadoList extends EntityQuery<Empleado> {

	private static final long serialVersionUID = 1L;
	
	@In(create = true)
	GestionPermisoVacacion gestionPermisoVacacion;
	
//	@In
//	EntityManager entityManager;
	
	private static final String EJBQL = "select empleado from Empleado empleado";
	private static final String ORDER = "empleado.apellido";

	private static final String[] RESTRICTIONS = {
		 	"empleado.usuarios.id = #{empleadoList.empleado.usuarios.id}",
			"lower(empleado.cedula) like concat(lower(#{empleadoList.empleado.cedula}),'%')",
			"lower(empleado.codigoEmpleado) like concat(lower(#{empleadoList.empleado.codigoEmpleado}),'%')",
			"lower(empleado.apellido) like concat(lower(#{empleadoList.empleado.apellido}),'%')",
			"lower(empleado.nombre) like concat(lower(#{empleadoList.empleado.nombre}),'%')",
			"lower(empleado.departamento.descripcion) = lower(#{empleadoList.empleado.departamento.descripcion})",
			"lower(empleado.cargo.descripcion) = lower(#{empleadoList.empleado.cargo.descripcion})",
			"lower(empleado.partida) like concat(lower(#{empleadoList.empleado.partida}),'%')",
			"lower(empleado.partidaIndividual) like concat(lower(#{empleadoList.empleado.partidaIndividual}),'%')",
			"empleado.empleado.emplId = #{empleadoList.empleado.empleado.emplId}",
			"lower(empleado.entradaSalida) like concat(lower(#{empleadoList.empleado.entradaSalida}),'%')",
			"empleado.ciudad.ciudId  in (#{empleadoList.ciudades})",
			"empleado.departamento.depaId = #{empleadoList.empleado.departamento.depaId}",
			"empleado.detalleTipoParametroByEstado.dtpaId = #{empleadoList.estado}",
			};
	
	private ArrayList<Long> ciudades;

	private String extensionArchivo;
	private Boolean gestionaPermiso;
	private int accesoEmpleados;
	 
	private String tipoReporte;
	private Long[] activoInactivo = {(long) 5};
	private Long[] controlVacacion = {(long) 1, (long) 0};
	private Long[] cargos;
	private Long[] detalleGrupoContratados;
	private Long[] departamentos;
	
	private Long[] ciudad;
	
	private Long estado;

	private Empleado empleado = new Empleado();

	public EmpleadoList() {
		setEjbql(EJBQL);
		setOrder(ORDER);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(Constantes.MAX_RESULTS);
		setExtensionArchivo(Constantes.PDF);
	}
	
	//Copiar
	public void asignarCargo(){
		
		this.setAccesoEmpleados(gestionPermisoVacacion.buscarAccesoEmpleados());
				
		if(this.getAccesoEmpleados() == 0){
			this.setTipoReporte("Empleado");
		}
		
		if(this.getTipoReporte().equals("")){
			this.setTipoReporte("Cargo");
		}
	}
	//Fin
	
	
	public List<Empleado> getListaEmpleadosAgrupadoDep(Long id) {
		
		this.setMaxResults(null);
		empleado = new Empleado();
		this.empleado.getDepartamento().setDepaId(id);
		return this.getResultList();
	}
	
	public List<Empleado> getListaEmpleadosAgrupadoDepAdmin(Long id) {
		
		List<Empleado> empleados = new ArrayList<Empleado>();
		Empleado empleado = gestionPermisoVacacion.buscarEmpleado();
		this.setAccesoEmpleados(gestionPermisoVacacion.buscarAccesoEmpleados());
		try {
			if (getAccesoEmpleados() == Constantes.ACCESO_TODOS)
				empleados.add(empleado);
			
			this.setMaxResults(null);
			empleado = new Empleado();
			this.empleado.getDepartamento().setDepaId(id);			
			
			empleados.addAll(this.getResultList());
			
		} catch (Exception e) {
			FacesMessages.instance().add("Error al cargar Empleado - Intente nuevamente");
			e.printStackTrace();
		}
		return empleados;
	}
			
	public Empleado getEmpleado() {
		return empleado;
	}
	
	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}
	
	public List<Empleado> getListaEmpleado2() {

		List<Empleado> empleados = new ArrayList<Empleado>();
		return empleados;
		
	}
	
	public List<Empleado> getListaEmpleado() {

		List<Empleado> empleados = new ArrayList<Empleado>();
		
		Empleado empleado = gestionPermisoVacacion.buscarEmpleado();
		this.setAccesoEmpleados(gestionPermisoVacacion.buscarAccesoEmpleados());
		try {
			if (getAccesoEmpleados() == Constantes.ACCESO_INDIVIDUAL ){
//				this.setEmpleado(empleado);
				this.getEmpleado().setEmplId(empleado.getEmplId());
				empleados.add(empleado);
			} else if (getAccesoEmpleados() == Constantes.ACCESO_SUBORDINADOS){
				this.getEmpleado().setEmpleado(empleado);
				empleados.add(empleado);
				
				this.setMaxResults(null);
				empleados.addAll(this.getResultList());
				
			} else if (getAccesoEmpleados() == Constantes.ACCESO_TODOS){
				this.buscarUsuarioCiudad();
				this.setMaxResults(null);
				empleados.addAll(this.getResultList());				
//				empleados = this.buscarEmpleadosCiudad();
			}			
		} catch (Exception e) {
			FacesMessages.instance().add("Error al cargar Empleado - Intente nuevamente");
			e.printStackTrace();
		}
		
		return empleados;
		
		
//		Empleado empleado = gestionPermisoVacacion.buscarEmpleado();
//		
//		this.setGestionaPermiso(gestionPermisoVacacion.buscarPermiso());
//		if (!isGestionaPermiso()) {
//			this.setEmpleado(empleado);
//		}		
//		
//		this.setMaxResults(null);
//		return this.getResultList();
	}
	
	public List<Empleado> getEmpleadoList() {

		List<Empleado> empleados = new ArrayList<Empleado>();
		
		Empleado empleado = gestionPermisoVacacion.buscarEmpleado();
		this.setAccesoEmpleados(gestionPermisoVacacion.buscarAccesoEmpleados());
		try {
			if (getAccesoEmpleados() == Constantes.ACCESO_INDIVIDUAL ){
				this.setEmpleado(empleado);
				empleados.add(empleado);
			} else if (getAccesoEmpleados() == Constantes.ACCESO_SUBORDINADOS){
				this.getEmpleado().setEmpleado(empleado);
				empleados.add(empleado);
				
				this.setMaxResults(Constantes.MAX_RESULTS);
				empleados.addAll(this.getResultList());
				
			} else if (getAccesoEmpleados() == Constantes.ACCESO_TODOS){
				this.buscarUsuarioCiudad();
				this.setMaxResults(Constantes.MAX_RESULTS);
				empleados.addAll(this.getResultList());				
			}			
		} catch (Exception e) {
			FacesMessages.instance().add("Error al cargar Empleado - Intente nuevamente");
			e.printStackTrace();
		}
		
		return empleados;
	}
	
	
	public void buscarUsuarioCiudad(){
		
		Empleado empleado = gestionPermisoVacacion.buscarEmpleado();
		
		Set<UsuarioCiudad> usuarioCiudades = empleado.getUsuarios().getUsuarioCiudads();
		for (UsuarioCiudad usuarioCiudad : usuarioCiudades) {
			this.getCiudades().add(usuarioCiudad.getCiudad().getCiudId());
		}
	}
	
//	@Factory
//	@SuppressWarnings("unchecked")
//	public List<Empleado> buscarEmpleadosCiudad() {
//		
//		return (List<Empleado>) entityManager
//				.createQuery(
//						"select e from Empleado e where e.ciudad.ciudId in (:ciudades) order by e.apellido")
//				.setParameter("ciudades", ciudades)
//				.getResultList();
//	}
	
	public String getExtensionArchivo() {
		return extensionArchivo;
	}

	public void setExtensionArchivo(String extensionArchivo) {
		this.extensionArchivo = extensionArchivo;
	}

	// ==================================================
	// ==================================================
	// ==================================================
	
	private String sortMode = "single";
	private String selectionMode = "multi";
	private SimpleSelection selection;
	private List<Empleado> selectedItems = new ArrayList<Empleado>();
	private ExtendedTableDataModel<Empleado> empleadoDataModel;
	
	@DataModel
	private List<Empleado> empleados;// = new ArrayList<Empleado>();
	
	@Factory
	public List<Empleado> listaDeEmpleados(){
		return this.getListaEmpleado();
	}
	
//	@Factory
//	public List<Empleado> listaDeEmpleadosDepartamento(Long id){
//		return this.getListaEmpleadosAgrupadoDep(id);
//	}
	
	@Factory
	public List<Empleado> listaDeEmpleadosDepartamento(Long id,Departamento departamento){
		
		List<Empleado> empleados=new ArrayList<Empleado>();
		empleados.addAll(this.getListaEmpleadosAgrupadoDep(id));
		
		if(departamento.getDepartamento()!=null){
			empleados.addAll(this.getListaEmpleadosAgrupadoDep(departamento.getDepartamento().getDepaId()));
		}
		
		return empleados;
	}	
	
	@Factory
	public List<Empleado> listaDeEmpleadosDepartamentoAdmin(Long id){
		return this.getListaEmpleadosAgrupadoDepAdmin(id);
	}
	
	public void takeSelection() {
		getSelectedItems().clear();
		Iterator<Object> iterator = getSelection().getKeys();
		while (iterator.hasNext()) {
			Object key = iterator.next();
//			getDataModel().setRowKey(key);
//			getSelectedItems().add((Empleado) getDataModel().getRowData());
			selectedItems.add(getEmpleadoDataModel().getObjectByKey(key));
		}
	}
	
	public List<Empleado> getListaEmpleadoMejorado() {

		List<Empleado> empleados = new ArrayList<Empleado>();
		
		Empleado empleado = gestionPermisoVacacion.buscarEmpleado();
		this.setAccesoEmpleados(gestionPermisoVacacion.buscarAccesoEmpleados());
		try {
			if (getAccesoEmpleados() == Constantes.ACCESO_INDIVIDUAL ){
//				this.setEmpleado(empleado);
				this.getEmpleado().setEmplId(empleado.getEmplId());
				empleados.add(empleado);
			} else if (getAccesoEmpleados() == Constantes.ACCESO_SUBORDINADOS){
				this.getEmpleado().setEmpleado(empleado);
				empleados.add(empleado);
				
				this.setMaxResults(null);
				empleados.addAll(this.getResultList());
				
			} else if (getAccesoEmpleados() == Constantes.ACCESO_TODOS){
				this.buscarUsuarioCiudad();
				this.setMaxResults(null);
				empleados.addAll(this.getResultList());				
//				empleados = this.buscarEmpleadosCiudad();
			}			
		} catch (Exception e) {
			FacesMessages.instance().add("Error al cargar Empleado - Intente nuevamente");
			e.printStackTrace();
		}
		
		List<Empleado> aux = new ArrayList<Empleado>();
		
		if(empleados.size()>0){
			for(Empleado e: empleados){
				if(e.getDetalleTipoParametroByEstado().getDtpaId()==5){
					Empleado em=new Empleado();
					em=e;
					aux.add(em);
				}				
			}
		}
		
		return aux;
	}

	
	
	public String getSortMode() {
		return sortMode;
	}

	public void setSortMode(String sortMode) {
		this.sortMode = sortMode;
	}

	public String getSelectionMode() {
		return selectionMode;
	}

	public void setSelectionMode(String selectionMode) {
		this.selectionMode = selectionMode;
	}

	public SimpleSelection getSelection() {
		return selection;
	}

	public void setSelection(SimpleSelection selection) {
		this.selection = selection;
	}
	
/*	public List<Empleado> getEmpleados() {
		return empleados;
	}

	public void setEmpleados(List<Empleado> empleados) {
		this.empleados = empleados;
	}
*/
	
//	@Factory(value = "empleados", scope = ScopeType.EVENT)
//	public List<Empleado> getEmpleados(){
//		List<Empleado> empleados = null;
//		if (empleados ==  null){
//			empleados = new ArrayList<Empleado>();
//		}
//		this.setMaxResults(null);
//		empleados = super.getResultList();
//		return empleados;
//		
//	}	
	public List<Empleado> getSelectedItems() {
		return selectedItems;
	}

	public void setSelectedItems(List<Empleado> selectedItems) {
		this.selectedItems = selectedItems;
	}

	public ExtendedTableDataModel<Empleado> getEmpleadoDataModel() {
		
		if (empleados == null) {
			empleados = this.listaDeEmpleados();
		}

		if (empleadoDataModel == null) {
			
			empleadoDataModel = new ExtendedTableDataModel<Empleado>(new DataProvider<Empleado>() {

						private static final long serialVersionUID = 1L;

						public Empleado getItemByKey(Object key) {
							
							
							for (Empleado e : empleados) {
								
								
								if (key.equals(getKey(e))) {
									return e;
								}
							}
							return null;
						}

						public List<Empleado> getItemsByRange(int firstRow,	int endRow) {
							return empleados.subList(firstRow, endRow);
						}

						public Object getKey(Empleado item) {
							return item.getEmplId();
						}

						public int getRowCount() {
							return empleados.size();
						}

					});
		}

		return empleadoDataModel;
	}
	
	public List<Empleado> getListaEmpleadoProgramaVacacion() {

		this.estado=null;
		this.estado=(long) 5;
		
		List<Empleado> empleados = new ArrayList<Empleado>();
		
		Empleado empleado = gestionPermisoVacacion.buscarEmpleado();
		this.setAccesoEmpleados(gestionPermisoVacacion.buscarAccesoEmpleados());
		try {
			if (getAccesoEmpleados() == Constantes.ACCESO_INDIVIDUAL ){
//				this.setEmpleado(empleado);
				this.getEmpleado().setEmplId(empleado.getEmplId());
				empleados.add(empleado);
			} else if (getAccesoEmpleados() == Constantes.ACCESO_SUBORDINADOS){
				this.getEmpleado().setEmpleado(empleado);
				empleados.add(empleado);
				
				this.setMaxResults(null);
				empleados.addAll(this.getResultList());
				
			} else if (getAccesoEmpleados() == Constantes.ACCESO_TODOS){
				this.buscarUsuarioCiudad();
				this.setMaxResults(null);
				empleados.addAll(this.getResultList());				
//				empleados = this.buscarEmpleadosCiudad();
			}			
		} catch (Exception e) {
			FacesMessages.instance().add("Error al cargar Empleado - Intente nuevamente");
			e.printStackTrace();
		}
		
		return empleados;
		
		
//		Empleado empleado = gestionPermisoVacacion.buscarEmpleado();
//		
//		this.setGestionaPermiso(gestionPermisoVacacion.buscarPermiso());
//		if (!isGestionaPermiso()) {
//			this.setEmpleado(empleado);
//		}		
//		
//		this.setMaxResults(null);
//		return this.getResultList();
	}	

	public void setEmpleadoDataModel(ExtendedTableDataModel<Empleado> empleadoDataModel) {
		this.empleadoDataModel = empleadoDataModel;
	}
	
	public Boolean isGestionaPermiso() {
		return gestionaPermiso;
	}

	public void setGestionaPermiso(Boolean gestionaPermiso) {
		this.gestionaPermiso = gestionaPermiso;
	}

	public int getAccesoEmpleados() {
		return accesoEmpleados;
	}

	public void setAccesoEmpleados(int accesoEmpleados) {
		this.accesoEmpleados = accesoEmpleados;
	}

	public ArrayList<Long> getCiudades() {
		if (ciudades == null){
			ciudades = new ArrayList<Long>();
		}
		return ciudades;
	}

	public void setCiudades(ArrayList<Long> ciudades) {
		this.ciudades = ciudades;
	}

	public String getTipoReporte() {
		if (tipoReporte == null){
			tipoReporte = new String();	
		}
		return tipoReporte;
	}

	public void setTipoReporte(String tipoReporte) {
		this.tipoReporte = tipoReporte;
	}

	public Long[] getActivoInactivo() {
		return activoInactivo;
	}

	public void setActivoInactivo(Long[] activoInactivo) {
		this.activoInactivo = activoInactivo;
	}

	public Long[] getControlVacacion() {
		return controlVacacion;
	}

	public void setControlVacacion(Long[] controlVacacion) {
		this.controlVacacion = controlVacacion;
	}

	public Long[] getCiudad() {
		return ciudad;
	}

	public void setCiudad(Long[] ciudad) {
		this.ciudad = ciudad;
	}

	public Long[] getCargos() {
		return cargos;
	}

	public void setCargos(Long[] cargos) {
		this.cargos = cargos;
	}

	public Long[] getDetalleGrupoContratados() {
		return detalleGrupoContratados;
	}

	public void setDetalleGrupoContratados(Long[] detalleGrupoContratados) {
		this.detalleGrupoContratados = detalleGrupoContratados;
	}

	public Long[] getDepartamentos() {
		return departamentos;
	}

	public void setDepartamentos(Long[] departamentos) {
		this.departamentos = departamentos;
	}
	
	public Long getEstado() {
		return estado;
	}

	public void setEstado(Long estado) {
		this.estado = estado;
	}
	
}