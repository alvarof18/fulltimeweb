package com.casapazmino.fulltime.anotaciones;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.hibernate.validator.Validator;
import org.jboss.seam.Component;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("uniqueValidator")
public class DuplicadoBean implements Validator<Duplicado> {

	String componente;
	String entidad;
	String campo;
	String campoId;

	public boolean isValid(Object valor) {
		EntityManager entityManager = (EntityManager) Component.getInstance("entityManager");
		EntityHome<?> entityHome = (EntityHome<?>) Component.getInstance(componente, false);

		Query query = entityManager.createQuery("select t from " + entidad
				+ " t where t." + campo + " = :valor and " + campoId + "<>:campoId");

//		Consulta cuando se edita un registro
		if (entityHome != null && entityHome.getId() != null) {
			query.setParameter("valor", valor);
			query.setParameter("campoId", entityHome.getId());
			
			
		} else {
//		Consulta cuando se crea un registro	
			query = entityManager.createQuery("select t from " + entidad
					+ " t where t." + campo + " = :valor");
			query.setParameter("valor", valor);
			
		}
		try {
			if (query.getResultList() != null && query.getResultList().size() > 0) {
				return false;
			}
			return true;
		} catch (final NoResultException e) {
			return true;
		}
	}

	@Override
	public void initialize(Duplicado duplicado) {
		this.componente = duplicado.componente();
		this.entidad = duplicado.entidad();
		this.campo = duplicado.campo();
		this.campoId = duplicado.campoId();
	}

/*	public boolean isValid(Object objeto) {
		try {

			EntityManager entityManager = (EntityManager) Component
					.getInstance("entityManager");
			Query query = entityManager.createNamedQuery(consulta);
			query.setParameter(1, (String) objeto);
			query.setParameter(2, null); // List result =
													// query.getResultList();

			if (query.getResultList() == null
					|| query.getResultList().size() == 0) {
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}
		
	@Override
	public void initialize(Duplicado duplicado) {
		this.consulta = duplicado.consulta();
	}
		
	}*/

}
