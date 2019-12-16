package network.warzone.tgm.modules.ctf.objective;

import network.warzone.tgm.TGM;
import network.warzone.tgm.modules.flag.FlagSubscriber;
import network.warzone.tgm.modules.flag.MatchFlag;
import network.warzone.tgm.modules.scoreboard.ScoreboardManagerModule;
import network.warzone.tgm.modules.team.MatchTeam;
import network.warzone.tgm.modules.team.TeamManagerModule;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Controls different objectives of CTF as a FlagSubscriber
 * Also more than likely handles scoreboard
 * Created by yikes on 12/15/2019
 */
public abstract class CTFController implements FlagSubscriber, Listener {
    private CTFControllerSubscriber subscriber;
    protected List<MatchFlag> allFlags;
    protected TeamManagerModule teamManagerModule;
    protected ScoreboardManagerModule scoreboardManagerModule;
    public CTFController(CTFControllerSubscriber subscriber, List<MatchFlag> allFlags) {
        this.subscriber = subscriber;
        this.allFlags = allFlags;
        this.teamManagerModule = TGM.get().getModule(TeamManagerModule.class);
        this.scoreboardManagerModule = TGM.get().getModule(ScoreboardManagerModule.class);
        TGM.registerEvents(this);
    }

    @Override
    public void pickup(MatchFlag flag, Player stealer) {
        stealer.getInventory().setHelmet(flag.generateBannerItem());
        MatchTeam team = teamManagerModule.getTeam(stealer);
        Bukkit.broadcastMessage(team.getColor() + stealer.getName() + ChatColor.GRAY
                + " stole " + flag.getTeam().getColor() + flag.getTeam().getAlias()
                + ChatColor.GRAY + "'s flag");
    }

    @Override
    public void drop(MatchFlag flag, Player stealer, Player attacker) {
        MatchTeam team = teamManagerModule.getTeam(stealer);
        Bukkit.broadcastMessage(team.getColor() + stealer.getName() + ChatColor.GRAY
                + " dropped " + flag.getTeam().getColor() + flag.getTeam().getAlias()
                + ChatColor.GRAY + "'s flag");
    }

    @Override
    public void capture(MatchFlag flag, Player capturer) {
        capturer.getInventory().setHelmet(new ItemStack(Material.AIR));
        Bukkit.broadcastMessage("gg a flag was captured by " + capturer.getName());
    }

    public void unload() {
        TGM.unregisterEvents(this);
    }

    public final void gameOver(MatchTeam team) {
        subscriber.gameOver(team);
    }
}
