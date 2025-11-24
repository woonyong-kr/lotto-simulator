package org.woonyong.lotto.central.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.woonyong.lotto.central.entity.Bot;
import org.woonyong.lotto.central.entity.Pos;
import org.woonyong.lotto.central.repository.BotRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class BotService {
    private static final String ERROR_BOT_NOT_FOUND = "존재하지 않는 봇입니다: ";
    private static final int DEFAULT_POS_COUNT = 3;

    private final BotRepository botRepository;
    private final PosService posService;

    public BotService(final BotRepository botRepository, final PosService posService) {
        this.botRepository = botRepository;
        this.posService = posService;
    }

    @Transactional
    public Bot createBot() {
        List<String> posUidList = createPosListForBot(DEFAULT_POS_COUNT);
        Long nextSequence = botRepository.findMaxId() + 1;
        Bot bot = Bot.create(nextSequence, posUidList);
        return botRepository.save(bot);
    }

    @Transactional
    public Bot updateConfig(final String botUid, final int intervalMs, final int ticketsPerPurchase) {
        Bot bot = findByBotUid(botUid);
        bot.updateConfig(intervalMs, ticketsPerPurchase);
        return bot;
    }

    @Transactional
    public Bot deactivate(final String botUid) {
        Bot bot = findByBotUid(botUid);
        bot.deactivate();
        deactivateBotPoses(bot);
        return bot;
    }

    public Bot getBot(final String botUid) {
        return findByBotUid(botUid);
    }

    public List<Bot> getActiveBotList() {
        return botRepository.findByActiveTrue();
    }

    public List<Bot> getAllBotList() {
        return botRepository.findAll();
    }

    private Bot findByBotUid(final String botUid) {
        return botRepository.findByBotUid(botUid)
                .orElseThrow(() -> new IllegalArgumentException(ERROR_BOT_NOT_FOUND + botUid));
    }

    private List<String> createPosListForBot(final int posCount) {
        List<String> posUidList = new ArrayList<>();
        for (int i = 0; i < posCount; i++) {
            Pos pos = posService.createPos();
            posUidList.add(pos.getPosUid());
        }
        return posUidList;
    }

    private void deactivateBotPoses(final Bot bot) {
        List<String> posUidList = bot.getPosUidList();
        for (String posUid : posUidList) {
            posService.updateStatus(posUid, false);
        }
    }
}
