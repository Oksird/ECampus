package ua.foxminded.muzychenko.dao.data.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import ua.foxminded.muzychenko.dao.GroupDao;
import ua.foxminded.muzychenko.dao.data.GroupsGenerator;
import ua.foxminded.muzychenko.entity.GroupEntity;

public class GroupsGeneratorImpl implements GroupsGenerator {

    private static final char[] ALPHABET = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final char[] NUMBERS = "0123456789".toCharArray();
    private static final char HYPHEN = '-';
    private final GroupDao groupDao;
    private final Random random = new Random();

    public GroupsGeneratorImpl(GroupDao groupDao) {
        this.groupDao = groupDao;
    }

    @Override

    public List<GroupEntity> generateData() {
        List<GroupEntity> groups = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            groups.add(
                new GroupEntity(i + 1L,
                    ALPHABET[random.nextInt(ALPHABET.length)] + ALPHABET[random.nextInt(
                        ALPHABET.length)] + HYPHEN + NUMBERS[random.nextInt(NUMBERS.length)]
                        + String.valueOf(NUMBERS[random.nextInt(NUMBERS.length)]) + "-" + i)
            );
        }
        return groups;
    }

    @Override
    public void insertGroups(List<GroupEntity> groups) {
        groups.forEach(groupDao::create);
    }
}
