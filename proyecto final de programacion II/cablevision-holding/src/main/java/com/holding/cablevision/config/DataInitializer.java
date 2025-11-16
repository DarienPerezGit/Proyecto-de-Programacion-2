package com.holding.cablevision.config;

import com.holding.cablevision.model.*;
import com.holding.cablevision.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

// @Component - DESACTIVADO: Base de datos vacía para ingreso manual de datos
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private PaisRepository paisRepository;
    
    @Autowired
    private AreaMercadoRepository areaMercadoRepository;
    
    @Autowired
    private EmpresaRepository empresaRepository;
    
    @Autowired
    private VendedorRepository vendedorRepository;
    
    @Autowired
    private AsesorRepository asesorRepository;
    
    @Autowired
    private CaptacionRepository captacionRepository;
    
    @Autowired
    private AsesorEmpresaAreaRepository asesorEmpresaAreaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println("=== Base de datos iniciada SIN datos de prueba ===");
        System.out.println("=== Listo para ingreso manual de datos ===");
        
        // DATOS DE PRUEBA DESACTIVADOS
        // Los datos deben ingresarse manualmente a través de la interfaz
    }
    
    /* MÉTODOS DE INICIALIZACIÓN DESACTIVADOS
    
    private void crearPaises() {
        if (paisRepository.count() == 0) {
            Pais argentina = new Pais();
            argentina.setNombre("Argentina");
            argentina.setPib(637485000000.0);
            argentina.setNumeroHabitantes(45195777L);
            argentina.setCapital("Buenos Aires");
            paisRepository.save(argentina);
            
            Pais brasil = new Pais();
            brasil.setNombre("Brasil");
            brasil.setPib(3608438000000.0);
            brasil.setNumeroHabitantes(215313498L);
            brasil.setCapital("Brasilia");
            paisRepository.save(brasil);
            
            Pais chile = new Pais();
            chile.setNombre("Chile");
            chile.setPib(317057000000.0);
            chile.setNumeroHabitantes(19458310L);
            chile.setCapital("Santiago");
            paisRepository.save(chile);
            
            System.out.println("Países creados: Argentina, Brasil, Chile");
        }
    }
    
    private void crearAreasMercado() {
        if (areaMercadoRepository.count() == 0) {
            AreaMercado telecom = new AreaMercado();
            telecom.setNombre("Telecomunicaciones");
            telecom.setDescripcion("Servicios de internet, telefonía y televisión por cable");
            areaMercadoRepository.save(telecom);
            
            AreaMercado streaming = new AreaMercado();
            streaming.setNombre("Streaming y Entretenimiento");
            streaming.setDescripcion("Plataformas de contenido digital y entretenimiento");
            areaMercadoRepository.save(streaming);
            
            AreaMercado fintech = new AreaMercado();
            fintech.setNombre("Fintech");
            fintech.setDescripcion("Tecnología financiera y medios de pago");
            areaMercadoRepository.save(fintech);
            
            System.out.println("Áreas de mercado creadas: Telecomunicaciones, Streaming, Fintech");
        }
    }
    
    private void crearEmpresas() {
        if (empresaRepository.count() == 0) {
            Pais argentina = paisRepository.findByNombre("Argentina").orElseThrow();
            Pais brasil = paisRepository.findByNombre("Brasil").orElseThrow();
            
            AreaMercado telecom = areaMercadoRepository.findByNombre("Telecomunicaciones").orElseThrow();
            AreaMercado streaming = areaMercadoRepository.findByNombre("Streaming y Entretenimiento").orElseThrow();
            
            // Empresa 1: Cablevisión Argentina
            Empresa cablevision = new Empresa();
            cablevision.setNombre("Cablevisión Argentina");
            cablevision.setFechaEntradaHolding(LocalDate.of(2010, 3, 15));
            cablevision.setFacturacionAnual(2500000000.0);
            cablevision.setNumeroVendedores(150);
            cablevision.setPaisSede(argentina);
            cablevision.setCiudadSede("Buenos Aires");
            cablevision.getPaisesOperacion().add(argentina);
            cablevision.getAreasMercado().add(telecom);
            empresaRepository.save(cablevision);
            
            // Empresa 2: Flow Entertainment
            Empresa flow = new Empresa();
            flow.setNombre("Flow Entertainment");
            flow.setFechaEntradaHolding(LocalDate.of(2015, 7, 22));
            flow.setFacturacionAnual(800000000.0);
            flow.setNumeroVendedores(75);
            flow.setPaisSede(argentina);
            flow.setCiudadSede("Córdoba");
            flow.getPaisesOperacion().add(argentina);
            flow.getAreasMercado().add(streaming);
            empresaRepository.save(flow);
            
            // Empresa 3: Telecom Brasil
            Empresa telecomBrasil = new Empresa();
            telecomBrasil.setNombre("Telecom Brasil");
            telecomBrasil.setFechaEntradaHolding(LocalDate.of(2018, 11, 8));
            telecomBrasil.setFacturacionAnual(1200000000.0);
            telecomBrasil.setNumeroVendedores(200);
            telecomBrasil.setPaisSede(brasil);
            telecomBrasil.setCiudadSede("São Paulo");
            telecomBrasil.getPaisesOperacion().add(brasil);
            telecomBrasil.getAreasMercado().add(telecom);
            empresaRepository.save(telecomBrasil);
            
            System.out.println("Empresas creadas: Cablevisión Argentina, Flow Entertainment, Telecom Brasil");
        }
    }
    
    private void crearVendedores() {
        if (vendedorRepository.count() == 0) {
            Empresa cablevision = empresaRepository.findFirstByNombre("Cablevisión Argentina").orElseThrow();
            Empresa flow = empresaRepository.findFirstByNombre("Flow Entertainment").orElseThrow();
            
            // Vendedores de Cablevisión
            Vendedor vendedor1 = new Vendedor();
            vendedor1.setCodigoVendedor("CV001");
            vendedor1.setNombre("Carlos Rodríguez");
            vendedor1.setDireccion("Av. Corrientes 1234, CABA");
            vendedor1.setEmpresa(cablevision);
            vendedorRepository.save(vendedor1);
            
            Vendedor vendedor2 = new Vendedor();
            vendedor2.setCodigoVendedor("CV002");
            vendedor2.setNombre("María González");
            vendedor2.setDireccion("Av. Santa Fe 5678, CABA");
            vendedor2.setEmpresa(cablevision);
            vendedor2.setVendedorSuperior(vendedor1);
            vendedorRepository.save(vendedor2);
            
            Vendedor vendedor3 = new Vendedor();
            vendedor3.setCodigoVendedor("CV003");
            vendedor3.setNombre("Luis Pérez");
            vendedor3.setDireccion("Av. Rivadavia 9876, CABA");
            vendedor3.setEmpresa(cablevision);
            vendedor3.setVendedorSuperior(vendedor1);
            vendedorRepository.save(vendedor3);
            
            // Vendedores de Flow
            Vendedor vendedor4 = new Vendedor();
            vendedor4.setCodigoVendedor("FL001");
            vendedor4.setNombre("Ana Martínez");
            vendedor4.setDireccion("Av. Colón 456, Córdoba");
            vendedor4.setEmpresa(flow);
            vendedorRepository.save(vendedor4);
            
            System.out.println("Vendedores creados: 4 vendedores en total");
            System.out.println("- CV001: Carlos Rodríguez (Supervisor)");
            System.out.println("- CV002: María González (Bajo CV001)");
            System.out.println("- CV003: Luis Pérez (Bajo CV001)");
            System.out.println("- FL001: Ana Martínez");
        }
    }
    
    private void crearAsesores() {
        if (asesorRepository.count() == 0) {
            Asesor asesor1 = new Asesor();
            asesor1.setCodigoAsesor("AS001");
            asesor1.setNombre("Dr. Roberto Silva");
            asesor1.setDireccion("Av. Libertador 2468, CABA");
            asesor1.setTitulacion("MBA en Telecomunicaciones");
            asesorRepository.save(asesor1);
            
            Asesor asesor2 = new Asesor();
            asesor2.setCodigoAsesor("AS002");
            asesor2.setNombre("Lic. Patricia López");
            asesor2.setDireccion("Av. 9 de Julio 1357, CABA");
            asesor2.setTitulacion("Lic. en Marketing Digital");
            asesorRepository.save(asesor2);
            
            System.out.println("Asesores creados:");
            System.out.println("- AS001: Dr. Roberto Silva (MBA en Telecomunicaciones)");
            System.out.println("- AS002: Lic. Patricia López (Lic. en Marketing Digital)");
        }
    }
    
    private void crearCaptaciones() {
        if (captacionRepository.count() == 0) {
            Vendedor carlos = vendedorRepository.findByCodigoVendedor("CV001").orElseThrow();
            Vendedor maria = vendedorRepository.findByCodigoVendedor("CV002").orElseThrow();
            Vendedor luis = vendedorRepository.findByCodigoVendedor("CV003").orElseThrow();
            
            // Carlos captó a María
            Captacion captacion1 = new Captacion();
            captacion1.setVendedor(carlos);
            captacion1.setVendedorCaptado(maria);
            captacion1.setFechaCaptacion(LocalDate.of(2023, 5, 15));
            captacionRepository.save(captacion1);
            
            // Carlos captó a Luis
            Captacion captacion2 = new Captacion();
            captacion2.setVendedor(carlos);
            captacion2.setVendedorCaptado(luis);
            captacion2.setFechaCaptacion(LocalDate.of(2023, 8, 22));
            captacionRepository.save(captacion2);
            
            System.out.println("Captaciones creadas:");
            System.out.println("- Carlos captó a María (15/05/2023)");
            System.out.println("- Carlos captó a Luis (22/08/2023)");
        }
    }
    
    private void crearAsignacionesAsesores() {
        if (asesorEmpresaAreaRepository.count() == 0) {
            Asesor roberto = asesorRepository.findByCodigoAsesor("AS001").orElseThrow();
            Asesor patricia = asesorRepository.findByCodigoAsesor("AS002").orElseThrow();
            
            Empresa cablevision = empresaRepository.findFirstByNombre("Cablevisión Argentina").orElseThrow();
            Empresa flow = empresaRepository.findFirstByNombre("Flow Entertainment").orElseThrow();
            
            AreaMercado telecom = areaMercadoRepository.findByNombre("Telecomunicaciones").orElseThrow();
            AreaMercado streaming = areaMercadoRepository.findByNombre("Streaming y Entretenimiento").orElseThrow();
            
            // Roberto asesora a Cablevisión en Telecomunicaciones
            AsesorEmpresaArea asignacion1 = new AsesorEmpresaArea();
            asignacion1.setAsesor(roberto);
            asignacion1.setEmpresa(cablevision);
            asignacion1.setAreaMercado(telecom);
            asignacion1.setFechaInicio(LocalDate.of(2023, 1, 10));
            asesorEmpresaAreaRepository.save(asignacion1);
            
            // Patricia asesora a Flow en Streaming
            AsesorEmpresaArea asignacion2 = new AsesorEmpresaArea();
            asignacion2.setAsesor(patricia);
            asignacion2.setEmpresa(flow);
            asignacion2.setAreaMercado(streaming);
            asignacion2.setFechaInicio(LocalDate.of(2023, 3, 20));
            asesorEmpresaAreaRepository.save(asignacion2);
            
            System.out.println("Asignaciones de asesores creadas:");
            System.out.println("- Roberto asesora Cablevisión en Telecomunicaciones");
            System.out.println("- Patricia asesora Flow en Streaming");
        }
    }
    
    FIN MÉTODOS DE INICIALIZACIÓN DESACTIVADOS */
}