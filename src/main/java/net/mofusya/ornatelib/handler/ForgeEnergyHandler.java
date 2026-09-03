package net.mofusya.ornatelib.handler;

import net.minecraftforge.energy.EnergyStorage;

public abstract class ForgeEnergyHandler extends EnergyStorage {
    public ForgeEnergyHandler(int capacity) {
        super(capacity, capacity, capacity, 0);
    }

    public ForgeEnergyHandler(int capacity, int maxTransfer) {
        super(capacity, maxTransfer, maxTransfer, 0);
    }

    public ForgeEnergyHandler(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract, 0);
    }

    public ForgeEnergyHandler(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int extractedEnergy = super.extractEnergy(maxExtract, simulate);
        if (extractedEnergy != 0) {
            onChanged();
        }

        return extractedEnergy;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int receiveEnergy = super.receiveEnergy(maxReceive, simulate);
        if (receiveEnergy != 0) {
            onChanged();
        }

        return receiveEnergy;
    }

    public int extractEnergyFromInside(int maxExtract, boolean simulate) {
        int energyExtracted = Math.min(energy, Math.min(this.getMaxEnergyStored(), maxExtract));
        if (!simulate) {
            energy -= energyExtracted;
        }

        if (energyExtracted != 0) {
            onChanged();
        }

        return energyExtracted;
    }

    public int receiveEnergyFromInsider(int maxReceive, boolean simulate) {
        int energyReceived = Math.min(capacity - energy, Math.min(this.getMaxEnergyStored(), maxReceive));
        if (!simulate) {
            energy += energyReceived;
        }

        if (energyReceived != 0) {
            onChanged();
        }

        return energyReceived;
    }

    public int setEnergy(int energy) {
        this.energy = energy;
        return energy;
    }

    public abstract void onChanged();
}
