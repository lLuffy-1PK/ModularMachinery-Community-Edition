package github.kasuminova.mmce.common.block.appeng;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public abstract class BlockMEItemBus extends BlockMEMachineComponent {

    @Override
    public void getDrops(@Nonnull NonNullList<ItemStack> result, @Nonnull IBlockAccess world, @Nonnull BlockPos pos,
                         @Nonnull IBlockState metadata, int fortune) {
        ItemStack stack = getRestorableDropItem(world, pos);
        if (!stack.isEmpty()) {
            result.add(stack);
        } else {
            super.getDrops(result, world, pos, metadata, fortune);
        }
    }

    @Override
    public boolean removedByPlayer(@Nonnull IBlockState state, @Nonnull World world, @Nonnull BlockPos pos,
                                   @Nonnull EntityPlayer player, boolean willHarvest) {
        return willHarvest || super.removedByPlayer(state, world, pos, player, willHarvest);
    }

    @Override
    public void harvestBlock(@Nonnull World world, @Nonnull EntityPlayer player, @Nonnull BlockPos pos,
                             @Nonnull IBlockState state, TileEntity te, @Nonnull ItemStack stack) {
        super.harvestBlock(world, player, pos, state, te, stack);
        world.setBlockToAir(pos);
    }

    @Nonnull
    protected abstract ItemStack getRestorableDropItem(@Nonnull IBlockAccess world, @Nonnull BlockPos pos);

    //    @Override
//    public boolean hasComparatorInputOverride(@Nonnull IBlockState state) {
//        return true;
//    }
//
//    @Override
//    public int getComparatorInputOverride(@Nonnull IBlockState blockState, World worldIn, @Nonnull BlockPos pos) {
//        return RedstoneHelper.getRedstoneLevel(worldIn.getTileEntity(pos));
//    }
}
