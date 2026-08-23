package com.CharunCore.server.worldgen.structure2;

import com.CharunCore.server.world.gen.RandomSource;
import com.CharunCore.server.worldgen.WorldGenLevel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SinglePoolElement extends StructurePoolElement {

    private final String templateId;

    public SinglePoolElement(String templateId, Projection projection) {
        super(projection);
        this.templateId = templateId;
    }

    public String getTemplateId() { return templateId; }

    private StructureTemplate getTemplate(StructureTemplateManager manager) {
        return manager.getOrCreate(templateId);
    }

    @Override
    public List<JigsawBlockInfo> getShuffledJigsawBlocks(
            StructureTemplateManager manager, int posX, int posY, int posZ,
            Rotation rotation, RandomSource random) {
        StructureTemplate template = getTemplate(manager);
        if (template == null) return Collections.emptyList();
        List<JigsawBlockInfo> jigsaws = template.getJigsaws();
        List<JigsawBlockInfo> result = new ArrayList<>(jigsaws.size());
        for (JigsawBlockInfo j : jigsaws) {
            int[] transformed = template.transformPosition(j.x(), j.y(), j.z(), rotation, Mirror.NONE);
            int stateId = BlockTransform.transform(j.blockStateId(), rotation, Mirror.NONE);
            result.add(new JigsawBlockInfo(
                posX + transformed[0], posY + transformed[1], posZ + transformed[2],
                stateId, j.nbt(), j.pool(), j.name(), j.target(),
                j.rollable(), j.placementPriority(), j.selectionPriority()));
        }
        Collections.shuffle(result, new java.util.Random(random.nextLong()));
        return result;
    }

    @Override
    public BoundingBox getBoundingBox(StructureTemplateManager manager,
            int posX, int posY, int posZ, Rotation rotation) {
        StructureTemplate template = getTemplate(manager);
        if (template == null) return new BoundingBox(posX, posY, posZ, posX, posY, posZ);
        return template.getBoundingBox(posX, posY, posZ, rotation);
    }

    @Override
    public void place(WorldGenLevel level, StructureTemplateManager manager,
            int posX, int posY, int posZ, Rotation rotation, Mirror mirror) {
        StructureTemplate template = getTemplate(manager);
        if (template == null) return;
        template.placeInWorld(level, posX, posY, posZ, rotation, mirror);
    }
}
