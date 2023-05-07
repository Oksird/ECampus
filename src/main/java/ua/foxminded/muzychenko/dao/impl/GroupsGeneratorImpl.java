package ua.foxminded.muzychenko.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import ua.foxminded.muzychenko.dao.GroupDao;
import ua.foxminded.muzychenko.dao.GroupsGenerator;
import ua.foxminded.muzychenko.entity.GroupEntity;

public class GroupsGeneratorImpl implements GroupsGenerator {

    private static final char[] ALPHABET = "abcdefghijklmnopqrstuvwxyz".toUpperCase().toCharArray();
    private static final char[] NUMBERS = "0123456789".toCharArray();
    private static final char HYPHEN = '-';
    private static final int COUNT_OF_GROUPS = 10;
    private final GroupDao groupDao;
    private final Random random;

    public GroupsGeneratorImpl(GroupDao groupDao, Random random) {
        this.groupDao = groupDao;
        this.random = random;
    }

    @Override

    public List<GroupEntity> generateData() {
        List<GroupEntity> groups = new ArrayList<>();
        for (int i = 0; i < COUNT_OF_GROUPS; i++) {
            groups.add(
                new GroupEntity(i + 1L,
                    String.valueOf(ALPHABET[random.nextInt(ALPHABET.length)]) + ALPHABET[random.nextInt(
                        ALPHABET.length)] + HYPHEN + NUMBERS[random.nextInt(NUMBERS.length)]
                        + NUMBERS[random.nextInt(NUMBERS.length)])
            );
        }
        return groups;
    }

    @Override
    public void insertGroups(List<GroupEntity> groups) {
        groups.forEach(groupDao::create);
    }
}
