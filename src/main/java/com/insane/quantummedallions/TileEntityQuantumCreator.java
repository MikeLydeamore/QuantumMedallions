package com.insane.quantummedallions;

import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityQuantumCreator extends TileEntity implements ISidedInventory {

	private ItemStack[] inventory = new ItemStack[2];
	public final int itemSlot = 0;
	public final int outputSlot=1;

	private ItemStack stackToMake=null;

	public TileEntityQuantumCreator()
	{

	}

	@Override
	public void updateEntity()
	{
		//System.out.println(this.getStackToCreate()==null ? "NULL" : this.getStackToCreate().getDisplayName());
		if (!worldObj.isRemote && inventory[itemSlot] != null)
		{
			if (this.stackToMake == null && inventory[itemSlot].getItem() instanceof ItemCompressible && ((inventory[itemSlot].getItemDamage()+1)%8) == 0)
			{
				this.setItemStackToCreate(QuantumMedallions.compressibles.get(inventory[itemSlot].getItemDamage()/8));
				this.sendPacket();
			}

			if (this.stackToMake != null)
			{
				if (inventory[outputSlot] == null)
				{
					inventory[outputSlot] = stackToMake.copy();
				}
				else if (inventory[outputSlot].getItem() == stackToMake.getItem() && inventory[outputSlot].getItemDamage() == stackToMake.getItemDamage())
				{
					if (inventory[outputSlot].stackSize < 64)
						inventory[outputSlot].stackSize++;
				}
			}
			this.markDirty();
			this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
		}
	}
	
	private void sendPacket() 
	{
		PacketHandler.INSTANCE.sendToAllAround(new MessageStackUpdate(xCoord, yCoord, zCoord, getStackToCreate()), getPacketRange());		
	}

	private TargetPoint getPacketRange() 
	{
		return new TargetPoint(worldObj.provider.dimensionId, xCoord, yCoord, zCoord, 5);
	}

	public void setItemStackToCreate(ItemStack stack)
	{
		if (stack == null)
			this.stackToMake = null;
		else
			this.stackToMake = stack.copy();
	}
	
	public ItemStack getStackToCreate()
	{
		if (this.stackToMake == null)
			return null;
		else
			return this.stackToMake.copy();
	}

	@Override
	public void writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);

		NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0; i < this.inventory.length; ++i)
		{	
			if (this.inventory[i] != null)
			{
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte)i);
				this.inventory[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}
		if (this.stackToMake != null)
		{
			NBTTagCompound createTag = new NBTTagCompound();
			this.stackToMake.writeToNBT(createTag);
			nbttaglist.appendTag(createTag);
		}

		tag.setTag("Items", nbttaglist);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		NBTTagList nbttaglist = tag.getTagList("Items", 10);
		this.inventory = new ItemStack[this.getSizeInventory()];
		int i;
		for (i = 0; i < nbttaglist.tagCount(); ++i)
		{
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			byte b0 = nbttagcompound1.getByte("Slot");

			if (b0 >= 0 && b0 < this.inventory.length)
			{
				this.inventory[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
		if (nbttaglist.getCompoundTagAt(i+1) != null)
			this.stackToMake = ItemStack.loadItemStackFromNBT(nbttaglist.getCompoundTagAt(i+1));
	}

	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound tag = new NBTTagCompound();
		this.writeToNBT(tag);

		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, this.blockMetadata, tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
	{
		NBTTagCompound tag = pkt.func_148857_g();
		this.readFromNBT(tag);
	}

	@Override
	public int getSizeInventory() 
	{
		return inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) 
	{
		return inventory[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) 
	{
		if (inventory[slot] != null)
		{
			if (inventory[slot].stackSize <= amount)
			{
				ItemStack stack = inventory[slot];
				inventory[slot] = null;
				return stack;
			}

			ItemStack itemstack1 = inventory[slot].splitStack(amount);
			if(inventory[slot].stackSize == 0)
			{
				inventory[slot] = null;
			}
			return itemstack1;
		}
		else
		{
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) 
	{
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) 
	{
		if (slot < getSizeInventory())
		{
			if (slot == 0)
			{
				if (stack == null)
					this.setItemStackToCreate(null);
				else
					this.setItemStackToCreate(QuantumMedallions.compressibles.get(stack.getItemDamage()/8));
			}
			inventory[slot]=stack;
		}
	}

	@Override
	public String getInventoryName() 
	{
		return null;
	}

	@Override
	public boolean hasCustomInventoryName() 
	{
		return false;
	}

	@Override
	public int getInventoryStackLimit() 
	{
		return 64;
	}
	@Override
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer) 
	{
		return worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory() 
	{

	}
	@Override
	public void closeInventory() 
	{

	}

	@Override
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) 
	{
		return false;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) 
	{
		int size = getSizeInventory();
		int[] slots = new int[size];
		for(int i = 1; i < size; i++)
		{
			slots[i] = i;
		}
		return slots;
	}

	@Override
	public boolean canInsertItem(int p_102007_1_, ItemStack p_102007_2_,
			int p_102007_3_) {
		return false;
	}
	@Override
	public boolean canExtractItem(int slot, ItemStack stack,
			int side) 
	{
		return slot==outputSlot;
	}
}
