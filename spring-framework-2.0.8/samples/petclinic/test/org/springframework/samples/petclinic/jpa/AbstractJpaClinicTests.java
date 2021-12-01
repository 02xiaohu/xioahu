package org.springframework.samples.petclinic.jpa;

import java.util.Collection;
import java.util.Date;

import javax.persistence.EntityManager;

import org.springframework.samples.petclinic.Clinic;
import org.springframework.samples.petclinic.Owner;
import org.springframework.samples.petclinic.Pet;
import org.springframework.samples.petclinic.PetType;
import org.springframework.samples.petclinic.Specialty;
import org.springframework.samples.petclinic.Vet;
import org.springframework.samples.petclinic.Visit;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.test.annotation.ExpectedException;
import org.springframework.test.jpa.AbstractJpaTests;

/**
 * This class extends {@link AbstractJpaTests}, one of the valuable test
 * superclasses provided in the <code>org.springframework.test</code> package.
 * This represents best practice for integration tests with Spring.
 *
 * <p>AbstractJpaTests and its superclasses provide the following services:
 * <li>Injects test dependencies, meaning that we don't need to perform application
 * context lookups. See the setClinic() method. Injection uses autowiring by type.
 * <li>Executes each test method in its own transaction, which is automatically
 * rolled back by default. This means that even if tests insert or otherwise
 * change database state, there is no need for a teardown or cleanup script.
 * <li>Provides useful inherited protected fields, such as a JdbcTemplate that can be
 * used to verify database state after test operations, or verify the results of queries
 * performed by application code. An ApplicationContext is also inherited, and can be
 * used for explicit lookup if necessary.
 *
 * <p>{@link AbstractJpaTests} and related classes are shipped in
 * <code>spring-mock.jar</code>.
 *
 * @author Rod Johnson
 * @see AbstractJpaTests
 */
public abstract class AbstractJpaClinicTests extends AbstractJpaTests {

	protected Clinic clinic;


	/**
	 * This method is provided to set the Clinic instance being tested by the Dependency Injection
	 * injection behaviour of the superclass from the <code>org.springframework.test</code> package.
	 * @param clinic clinic to test
	 */
	public void setClinic(Clinic clinic) {
		this.clinic = clinic;
	}


	@ExpectedException(IllegalArgumentException.class)
	public void testBogusJpql() {
		sharedEntityManager.createQuery("SELECT RUBBISH FROM RUBBISH HEAP").executeUpdate();
	}

	public void testApplicationManaged() {
		EntityManager appManaged = entityManagerFactory.createEntityManager();
		appManaged.joinTransaction();
	}

	public void testGetVets() {
		Collection vets = this.clinic.getVets();
		
		// Use the inherited JdbcTemplate (from AbstractTransactionalDataSourceSpringContextTests) 
		// to verify the results of the query
		assertEquals("JDBC query must show the same number of vets",
				jdbcTemplate.queryForInt("SELECT COUNT(0) FROM VETS"), 
				vets.size());
		Vet v1 = (Vet) EntityUtils.getById(vets, Vet.class, 2);
		assertEquals("Leary", v1.getLastName());
		assertEquals(1, v1.getNrOfSpecialties());
		assertEquals("radiology", ((Specialty) v1.getSpecialties().get(0)).getName());
		Vet v2 = (Vet) EntityUtils.getById(vets, Vet.class, 3);
		assertEquals("Douglas", v2.getLastName());
		assertEquals(2, v2.getNrOfSpecialties());
		assertEquals("dentistry", ((Specialty) v2.getSpecialties().get(0)).getName());
		assertEquals("surgery", ((Specialty) v2.getSpecialties().get(1)).getName());
	}

	public void testGetPetTypes() {
		Collection petTypes = this.clinic.getPetTypes();
		assertEquals("JDBC query must show the same number of pet typess",
				jdbcTemplate.queryForInt("SELECT COUNT(0) FROM TYPES"), 
				petTypes.size());
		PetType t1 = (PetType) EntityUtils.getById(petTypes, PetType.class, 1);
		assertEquals("cat", t1.getName());
		PetType t4 = (PetType) EntityUtils.getById(petTypes, PetType.class, 4);
		assertEquals("snake", t4.getName());
	}

	public void testFindOwners() {
		Collection owners = this.clinic.findOwners("Davis");
		assertEquals(2, owners.size());
		owners = this.clinic.findOwners("Daviss");
		assertEquals(0, owners.size());
	}

//	public void testLoadOwner() {
//		Owner o1 = this.clinic.loadOwner(1);
//		assertTrue(o1.getLastName().startsWith("Franklin"));
//		Owner o10 = this.clinic.loadOwner(10);
//		assertEquals("Carlos", o10.getFirstName());
//		
//		// Check lazy loading, by ending the transaction
//		endTransaction();
//		// Now Owners are "disconnected" from the data store.
//		// We might need to touch this collection if we switched to lazy loading
//		// in mapping files, but this test would pick this up.
//		o1.getPets();
//	}

	public void testInsertOwner() {
		Collection owners = this.clinic.findOwners("Schultz");
		int found = owners.size();
		Owner owner = new Owner();
		owner.setLastName("Schultz");
		this.clinic.storeOwner(owner);
		// assertTrue(!owner.isNew());  -- NOT TRUE FOR TOPLINK (before commit)
		owners = this.clinic.findOwners("Schultz");
		assertEquals(found + 1, owners.size());
	}

	public void testUpdateOwner() throws Exception {
		Owner o1 = this.clinic.loadOwner(1);
		String old = o1.getLastName();
		o1.setLastName(old + "X");
		this.clinic.storeOwner(o1);
		o1 = this.clinic.loadOwner(1);
		assertEquals(old + "X", o1.getLastName());
	}

	public void testLoadPet() {
		Collection types = this.clinic.getPetTypes();
		Pet p7 = this.clinic.loadPet(7);
		assertTrue(p7.getName().startsWith("Samantha"));
		assertEquals(EntityUtils.getById(types, PetType.class, 1).getId(), p7.getType().getId());
		assertEquals("Jean", p7.getOwner().getFirstName());
		Pet p6 = this.clinic.loadPet(6);
		assertEquals("George", p6.getName());
		assertEquals(EntityUtils.getById(types, PetType.class, 4).getId(), p6.getType().getId());
		assertEquals("Peter", p6.getOwner().getFirstName());
	}

	public void testInsertPet() {
		Owner o6 = this.clinic.loadOwner(6);
		int found = o6.getPets().size();
		Pet pet = new Pet();
		pet.setName("bowser");
		Collection types = this.clinic.getPetTypes();
		pet.setType((PetType) EntityUtils.getById(types, PetType.class, 2));
		pet.setBirthDate(new Date());
		o6.addPet(pet);
		assertEquals(found + 1, o6.getPets().size());
		this.clinic.storeOwner(o6);
		// assertTrue(!pet.isNew());  -- NOT TRUE FOR TOPLINK (before commit)
		o6 = this.clinic.loadOwner(6);
		assertEquals(found + 1, o6.getPets().size());
	}

	public void testUpdatePet() throws Exception {
		Pet p7 = this.clinic.loadPet(7);
		String old = p7.getName();
		p7.setName(old + "X");
		this.clinic.storePet(p7);
		p7 = this.clinic.loadPet(7);
		assertEquals(old + "X", p7.getName());
	}

	public void testInsertVisit() {
		Pet p7 = this.clinic.loadPet(7);
		int found = p7.getVisits().size();
		Visit visit = new Visit();
		p7.addVisit(visit);
		visit.setDescription("test");
		this.clinic.storePet(p7);
		// assertTrue(!visit.isNew());  -- NOT TRUE FOR TOPLINK (before commit)
		p7 = this.clinic.loadPet(7);
		assertEquals(found + 1, p7.getVisits().size());
	}

}
