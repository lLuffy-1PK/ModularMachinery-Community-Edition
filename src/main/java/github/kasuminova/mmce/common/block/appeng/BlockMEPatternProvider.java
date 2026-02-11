package github.kasuminova.mmce.common.block.appeng;

import github.kasuminova.mmce.common.tile.MEPatternProvider;
import hellfirepvp.modularmachinery.ModularMachinery;
import hellfirepvp.modularmachinery.common.CommonProxy;
import hellfirepvp.modularmachinery.common.lib.ItemsMM;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockMEPatternProvider extends BlockMEMachineComponent {

    @Override
    public boolean onBlockActivated(
            @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state,
            @Nonnull EntityPlayer playerIn, @Nonnull EnumHand hand,
            @Nonnull EnumFacing facing,
            float hitX, float hitY, float hitZ)
    {
        if (!worldIn.isRemote) {
            TileEntity te = worldIn.getTileEntity(pos);
            if (te instanceof MEPatternProvider) {
                playerIn.openGui(ModularMachinery.MODID, CommonProxy.GuiType.ME_PATTERN_PROVIDER.ordinal(), worldIn, pos.getX(), pos.getY(), pos.getZ());
            }
        }
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(final World world, final IBlockState state) {
        return new MEPatternProvider();
    }

    @Override
    public void getDrops(@Nonnull NonNullList<ItemStack> drops,
                         @Nonnull IBlockAccess world,
                         @Nonnull BlockPos pos,
                         @Nonnull IBlockState state,
                         int fortune)
    {
        TileEntity te = world.getTileEntity(pos);
        if (!(te instanceof MEPatternProvider provider) || provider.isAllDefault()) {
            super.getDrops(drops, world, pos, state, fortune);
            return;
        }

        ItemStack dropped = new ItemStack(ItemsMM.mePatternProvider);
        dropped.setTagInfo("patternProvider", provider.writeProviderNBT(new NBTTagCompound()));

        drops.add(dropped);
    }

    @Override
    public boolean removedByPlayer(@Nonnull IBlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull EntityPlayer player, boolean willHarvest) {
        return willHarvest || super.removedByPlayer(state, world, pos, player, willHarvest);
    }

    @Override
    public void harvestBlock(@Nonnull World world, @Nonnull EntityPlayer player, @Nonnull BlockPos pos, @Nonnull IBlockState state, TileEntity te, @Nonnull ItemStack stack) {
        super.harvestBlock(world, player, pos, state, te, stack);
        world.setBlockToAir(pos);
    }

    @Override
    public void onBlockPlacedBy(@Nonnull final World worldIn,
                                @Nonnull final BlockPos pos,
                                @Nonnull final IBlockState state,
                                @Nonnull final EntityLivingBase placer,
                                @Nonnull final ItemStack stack)
    {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

        TileEntity te = worldIn.getTileEntity(pos);
        NBTTagCompound tag = stack.getTagCompound();
        if (te instanceof final MEPatternProvider provider && tag != null && tag.hasKey("patternProvider")) {
            provider.readProviderNBT(tag.getCompoundTag("patternProvider"));
        }
    }

}
