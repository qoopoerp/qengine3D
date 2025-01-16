/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.physics.vehiculo;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.EntityComponent;
import net.qoopo.engine.core.entity.component.physics.dinamica.QObjetoRigido;

/**
 * Clase que representa un vehiculo para la gestión de la física
 *
 * @author alberto
 */
public class QVehiculo implements EntityComponent {

    @Getter
    @Setter
    private Entity entity;

    private QObjetoRigido chasis;
    private List<QRueda> ruedas = null;
    private float gEngineForce = 0.0f;
    private float gBreakingForce = 0;
    private float gVehicleSteering = 0;

    public QVehiculo(QObjetoRigido chasis) {
        this.chasis = chasis;
        chasis.setUsado(true);
    }

    public void agregarRueda(QRueda rueda) {
        if (ruedas == null) {
            ruedas = new ArrayList<>();
        }
        ruedas.add(rueda);
    }

    public QObjetoRigido getChasis() {
        return chasis;
    }

    public void setChasis(QObjetoRigido chasis) {
        this.chasis = chasis;
    }

    public List<QRueda> getRuedas() {
        return ruedas;
    }

    public void setRuedas(List<QRueda> ruedas) {
        this.ruedas = ruedas;
    }

    @Override
    public void destroy() {
        chasis.setUsado(false);
    }

    public float getgEngineForce() {
        return gEngineForce;
    }

    public void setgEngineForce(float gEngineForce) {
        this.gEngineForce = gEngineForce;
    }

    public float getgBreakingForce() {
        return gBreakingForce;
    }

    public void setgBreakingForce(float gBreakingForce) {
        this.gBreakingForce = gBreakingForce;
    }

    public float getgVehicleSteering() {
        return gVehicleSteering;
    }

    public void setgVehicleSteering(float gVehicleSteering) {
        this.gVehicleSteering = gVehicleSteering;
    }

}
