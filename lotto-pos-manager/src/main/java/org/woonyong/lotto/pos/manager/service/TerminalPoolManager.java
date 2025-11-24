package org.woonyong.lotto.pos.manager.service;

import org.springframework.stereotype.Component;
import org.woonyong.lotto.pos.manager.domain.TerminalInfo;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TerminalPoolManager {
    private final Set<TerminalInfo> availableTerminals = ConcurrentHashMap.newKeySet();
    private final Map<String, TerminalInfo> allTerminals = new ConcurrentHashMap<>();

    public void register(final String terminalId, final String address) {
        TerminalInfo info = new TerminalInfo(terminalId, address);
        allTerminals.put(terminalId, info);
        availableTerminals.add(info);
    }

    public void register(final String terminalId, final String address, final String containerId) {
        TerminalInfo info = new TerminalInfo(terminalId, address, containerId);
        allTerminals.put(terminalId, info);
        availableTerminals.add(info);
    }

    public TerminalInfo allocate() {
        Iterator<TerminalInfo> iterator = availableTerminals.iterator();
        if (!iterator.hasNext()) {
            return null;
        }

        TerminalInfo terminal = iterator.next();
        availableTerminals.remove(terminal);
        return terminal;
    }

    public void release(final String terminalId) {
        TerminalInfo info = allTerminals.get(terminalId);
        if (info != null) {
            availableTerminals.add(info);
        }
    }

    public void remove(final String terminalId) {
        TerminalInfo info = allTerminals.remove(terminalId);
        if (info != null) {
            availableTerminals.remove(info);
        }
    }

    public TerminalInfo findByTerminalId(final String terminalId) {
        return allTerminals.get(terminalId);
    }

    public int getAvailableCount() {
        return availableTerminals.size();
    }

    public int getTotalInstanceCount() {
        return allTerminals.size();
    }

    public Iterable<TerminalInfo> getAllTerminals() {
        return allTerminals.values();
    }

    public boolean hasAvailableTerminals() {
        return !availableTerminals.isEmpty();
    }
}