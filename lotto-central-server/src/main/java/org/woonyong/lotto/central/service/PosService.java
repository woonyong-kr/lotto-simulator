package org.woonyong.lotto.central.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.woonyong.lotto.central.entity.Pos;
import org.woonyong.lotto.central.repository.PosRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PosService {
    private static final String ERROR_POS_NOT_FOUND = "존재하지 않는 POS입니다: ";

    private final PosRepository posRepository;

    public PosService(final PosRepository posRepository) {
        this.posRepository = posRepository;
    }

    @Transactional
    public Pos createPos() {
        Long nextSequence = posRepository.findMaxId() + 1;
        Pos pos = Pos.create(nextSequence);
        return posRepository.save(pos);
    }

    @Transactional
    public Pos updateStatus(final String posUid, final boolean active) {
        Pos pos = findByPosUid(posUid);
        updateActiveStatus(pos, active);
        return pos;
    }

    public Pos getPos(final String posUid) {
        return findByPosUid(posUid);
    }

    public List<Pos> getActivePosList() {
        return posRepository.findByActiveTrue();
    }

    public List<Pos> getAllPosList() {
        return posRepository.findAll();
    }

    private Pos findByPosUid(final String posUid) {
        return posRepository.findByPosUid(posUid)
                .orElseThrow(() -> new IllegalArgumentException(ERROR_POS_NOT_FOUND + posUid));
    }

    private void updateActiveStatus(final Pos pos, final boolean active) {
        if (active) {
            pos.activate();
            return;
        }
        pos.deactivate();
    }
}