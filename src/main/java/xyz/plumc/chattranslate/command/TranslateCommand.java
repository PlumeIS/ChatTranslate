package xyz.plumc.chattranslate.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import xyz.plumc.chattranslate.ChatTranslate;
import xyz.plumc.chattranslate.Config;
import xyz.plumc.chattranslate.mixin.BookViewScreenAccessor;
import xyz.plumc.chattranslate.mixin.MinecraftInvoker;

import java.util.ArrayList;
import java.util.List;

public class TranslateCommand {
    public TranslateCommand(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("translate").executes(TranslateCommand::translate));
    }

    private static int translate(CommandContext<CommandSourceStack> context) {
        LocalPlayer player = Minecraft.getInstance().player;
        ItemStack book = player.getInventory().getSelected();
        if (book.is(Items.WRITTEN_BOOK)) {
            translateBook(BookViewScreen.BookAccess.fromItem(book), book);
        }

        Vec3 playerPos = player.getEyePosition(1.0F);
        Vec3 lookVec = player.getLookAngle();
        double maxDistance = 5.0D;
        Vec3 endPos = playerPos.add(lookVec.x * maxDistance, lookVec.y * maxDistance, lookVec.z * maxDistance);
        BlockHitResult rayTraceResult = Minecraft.getInstance().level.clip(new ClipContext(playerPos, endPos, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
        if (rayTraceResult.getType()== HitResult.Type.BLOCK){
            BlockPos blockPos = rayTraceResult.getBlockPos();
            BlockEntity blockEntity = Minecraft.getInstance().level.getBlockEntity(blockPos);
            if (blockEntity instanceof LecternBlockEntity){
                Minecraft minecraft = Minecraft.getInstance();
                ((MinecraftInvoker) minecraft).useItem();
                ChatTranslate.TickTaskHandler.runOnNextClientTick(()->{
                    BookViewScreen.BookAccess bookAccess = ((BookViewScreenAccessor) minecraft.screen).getBookAccess();
                    ItemStack bookSamp = new ItemStack(Items.WRITTEN_BOOK);
                    CompoundTag compoundTag = new CompoundTag();
                    compoundTag.putString("title", "");
                    compoundTag.putString("author", "");
                    bookSamp.setTag(compoundTag);
                    translateBook(bookAccess, bookSamp);
                });
            }
            if (blockEntity instanceof SignBlockEntity sign){
                StringBuilder stringBuilder = new StringBuilder();

                for (Component message:sign.getFrontText().getMessages(true)){
                    stringBuilder.append(message.getString());
                    stringBuilder.append(" ");
                }
                new Thread(()-> {
                    String translated = Config.translator.translate(stringBuilder.toString(), Config.langFrom, Config.langTo);
                    Minecraft.getInstance().player.sendSystemMessage(Component.literal(ChatTranslate.translatePrefix.formatted(Config.langTo.name) + translated));
                }).start();
            }
        }
        return 1;
    }

    private static void translateBook(BookViewScreen.BookAccess bookAccess, ItemStack book) {
        List<String> translatedPages = new ArrayList<>();
        new Thread(()->{
        for (int i = 0; i < bookAccess.getPageCount(); i++) {
            translatedPages.add(Config.translator.translate(bookAccess.getPage(i).getString(), Config.langFrom, Config.langTo));
        }
        ChatTranslate.TickTaskHandler.runOnNextClientTick(()->{
            ItemStack translateBook = new ItemStack(Items.WRITTEN_BOOK);
            CompoundTag itemTag = book.getTag().copy();
            ListTag pagesTag = new ListTag();
            for (String i : translatedPages) {
                pagesTag.add(StringTag.valueOf("{\"text\":\"%s\"}".formatted(i)));
            }
            itemTag.put("pages", pagesTag);
            translateBook.setTag(itemTag);
            BookViewScreen screen = new BookViewScreen(new BookViewScreen.WrittenBookAccess(translateBook));
            Minecraft.getInstance().setScreen(screen);
        });
        }).start();
    }
}
