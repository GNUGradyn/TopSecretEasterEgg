package com.gradyn.topsecreteasteregg;

import okhttp3.*;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class PlayerDeathEventHandler implements Listener {
    @EventHandler
    public void OnPlayerDeath(PlayerDeathEvent event) {
        if (!event.getPlayer().getUniqueId().equals(UUID.fromString(TopSecretEasterEgg.inst.getConfig().getString("MinecraftUuid")))) return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("application/json");
                String causeOfDeath = event.deathMessage().toString().replace(event.getPlayer().getName(), "").trim();
                HashMap inventory = new HashMap<Material, Integer>();
                for (ItemStack item : event.getPlayer().getInventory()) {
                    if (inventory.containsKey(item.getType())) {
                        inventory.replace(item.getType(), ((int)inventory.get(item.getType())) + item.getAmount());
                    } else {
                        inventory.put(item.getType(), 0);
                    }
                }
                String inventoryString = String.join("\n", (String[])inventory.keySet().stream().map(x -> ((Integer)inventory.get(x)).toString() + "x " + x.toString()).toArray(String[]::new));
                String message = "The player has died in minecraft. Write them a message about their death, taking into account the following information about their cause of death and inventory. Example: \"Ouch, those creepers sure do sneak up on you! I hope you're able to recover your 512 stone!\"\nCause of death: " + causeOfDeath + "\nInventory:\n" + inventoryString;
                WebhookRequest request = new WebhookRequest(message, new long[] {TopSecretEasterEgg.inst.getConfig().getLong("DiscordId")}, TopSecretEasterEgg.inst.getConfig().getBoolean("TestMode"));
                RequestBody body = RequestBody.create(mediaType, );
                Request request = new Request.Builder()
                        .url("https://birdapi.hbigroup.org/customerservice")
                        .method("POST", body)
                        .addHeader("Authorization", "Bearer " + TopSecretEasterEgg.inst.getConfig().getString("BirdToken"))
                        .addHeader("Content-Type", "application/json")
                        .build();
                try {
                    client.newCall(request).execute();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).run();
    }

    class WebhookRequest {
        String message;
        long[] pings;
        boolean testMode;

        public WebhookRequest(String message, long[] pings, boolean testMode) {
            this.message = message;
            this.pings = pings;
            this.testMode = testMode;
        }

        public long[] getPings() {
            return pings;
        }

        public void setPings(long[] pings) {
            this.pings = pings;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public boolean isTestMode() {
            return testMode;
        }

        public void setTestMode(boolean testMode) {
            this.testMode = testMode;
        }
    }
}
