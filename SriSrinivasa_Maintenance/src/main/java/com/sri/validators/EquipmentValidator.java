package com.sri.validators;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.sri.Entity.Equipment;

@Component
public class EquipmentValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(Equipment.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Equipment equipment = (Equipment) target;

        if (equipment.getName() == null || equipment.getName().isBlank()) {
            errors.rejectValue("name", "equipment.name.null");
        }
        if (equipment.getManufacturer() == null || equipment.getManufacturer().isBlank()) {
            errors.rejectValue("manufacturer", "equipment.manufacturer.null");
        }
        if (equipment.getModelNumber() == null || equipment.getModelNumber().isBlank()) {
            errors.rejectValue("modelNumber", "equipment.modelNumber.null");
        }
        if (equipment.getWarrantyInformation() == null || equipment.getWarrantyInformation().isBlank()) {
            errors.rejectValue("warrantyInformation", "equipment.warrantyInformation.null");
        }
        if (equipment.getCriticalityLevel() == null || equipment.getCriticalityLevel().isBlank()) {
            errors.rejectValue("criticalityLevel", "equipment.criticalityLevel.null");
        }
        if (equipment.getOperationalStatus() == null || equipment.getOperationalStatus().isBlank()) {
            errors.rejectValue("operationalStatus", "equipment.operationalStatus.null");
        }
        if (equipment.getMaintenanceFrequency() == null || equipment.getMaintenanceFrequency().isBlank()) {
            errors.rejectValue("maintenanceFrequency", "equipment.maintenanceFrequency.null");
        }
        if (equipment.getOperatingHours() < 0) {
            errors.rejectValue("operatingHours", "equipment.operatingHours.invalid");
        }
        if (equipment.getCost() < 0) {
            errors.rejectValue("cost", "equipment.cost.invalid");
        }
        if (equipment.getNotes() == null || equipment.getNotes().isBlank()) {
            errors.rejectValue("notes", "equipment.notes.null");
        }
        if (equipment.getLocation() == null) {
            errors.rejectValue("location", "equipment.location.null");
        }
    }
}
