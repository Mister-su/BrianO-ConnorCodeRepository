package com.dinfo.oec.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.dinfo.init.ControlConfig;
import com.dinfo.oec.bean.JobGroup;
import com.dinfo.oec.bean.JobItem;
import com.dinfo.oec.bean.OntologyInfo;
import com.dinfo.oec.bean.ParseConfig;
import com.dinfo.oec.bean.ParseInfo;
import com.dinfo.oec.util.CommonUtil;
import com.dinfo.oec.util.Utils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class ParseCommon {

	private AnalyClassify classify;
    private static final Logger log = Logger.getLogger(ParseCommon.class);
//	public void parseJobItemByExp(JobItem item, String exp) {
//		classify = new AnalyClassify();
//		String content = item.getContent();
//		if (null == content)
//			return;
//		content = CommonUtil.filterWord(content);
//		List<String> segs = CommonUtil.segContent(content);
//		TreeMap<String, ArrayList<String>>  elementValues = Maps.newTreeMap();
//		TreeMap<String, ArrayList<String>>  conceptValues =  Maps.newTreeMap();
//		ArrayList<String> elementCate = classify.classElement(segs, content,elementValues);
//		ArrayList<String> conceptCate = classify.classConcept(segs, content,conceptValues);
//		String classresult = CommonUtil.toCateResult(conceptCate, elementCate,
//				content);
//		Map<Integer, String> cates = new HashMap<Integer, String>();
//		cates.put(-1, classresult);
//		cates.put(0, classresult);
//		ParseInfo info = classify.classOntologyByExp(cates, segs, conceptCate,
//				elementCate, exp);
//		//分析意图。
//		//这里取出一个
//		List<String> cas = info.getCatlogs();
//		List<Integer> intentions= Lists.newArrayList();
//		for(String ca:cas){
//			intentions.add(classify.intentionParse(ca,cates, segs, conceptCate,elementCate));						
//		}
//		info.setIntentions(intentions);
//		info.setConceptValueList(CommonUtil.toConceptValue(conceptValues));
//		info.setElementValueList(CommonUtil.toElementValue(elementValues));
//
//		info.setConcepts(CommonUtil.toConceptName(conceptCate));
//		info.setElements(CommonUtil.toElementName(elementCate));
//		List<String> Ontology = classify.getOntoPathByOntologyids(info
//				.getCatlogs());
//		item.setCatlogPath(Ontology);
//		item.setParseInfo(info);
//	}

	public ParseInfo parseJobItem(JobItem item,String pcate_id, int level) {
		Long time = System.currentTimeMillis();
		ControlConfig.local.set(time);
		log.info("开始处理："+time+":"+item.getContent());
		classify = new AnalyClassify();
		String content = item.getContent();
		if (null == content)
			return null;
		content = CommonUtil.filterWord(content);
		List<String> segwords = new ArrayList<String>();
		List<String> segs = CommonUtil.segContent(content);
		segwords.add(Utils.concatWithEnd(segs, " "));
		TreeMap<String, ArrayList<String>> elementValues = Maps.newTreeMap();
		TreeMap<String, ArrayList<String>> conceptValues = Maps.newTreeMap();
		ArrayList<String> elementCate = classify.classElement(segs, content,elementValues);
		ArrayList<String> conceptCate = classify.classConcept(segs, content,conceptValues);
		String classresult = CommonUtil.toCateResult(conceptCate, elementCate,
				content);
		Map<Integer, String> cates = new HashMap<Integer, String>();
		cates.put(-1, classresult);
		cates.put(0, classresult);
	
		ParseInfo info = classify.classOntology(cates, segs, conceptCate,
				elementCate,pcate_id,level);
		//分析意图。
		//这里取出一个
		List<String> cas = info.getCatlogs();
		List<Integer> intentions= Lists.newArrayList();
//		for(String ca:cas){
//			intentions.add(classify.intentionParse(ca,cates, segs, conceptCate,elementCate));						
//		}
//		info.setIntentions(intentions);
		info.setConcepts(CommonUtil.toConceptName(conceptCate));
		info.setElements(CommonUtil.toElementName(elementCate));
		info.setSegwords(segwords);
		info.setConceptValueList(CommonUtil.toConceptValue(conceptValues));
		info.setElementValueList(CommonUtil.toElementValue(elementValues));
		
		info.setConceptValueMap(CommonUtil.returnConceptValueMap(conceptValues));
		info.setElementValueMap(CommonUtil.returnElementValueMap(elementValues));
		List<String> Ontology = classify.getOntoPathByOntologyids(info
				.getCatlogs());
		info.setCatlogPath(Ontology);
		CommonUtil.innerCodeShared(info);
		CommonUtil.sortOntology(info);
		return info;
	}

	public ParseInfo parseJobGroup(JobGroup group,String pcate_id, int level) {
		classify = new AnalyClassify();
		List<String> segwords = new ArrayList<String>();
		Set<String> cates = new HashSet<String>();
		Map<Integer, String> catemaps = new HashMap<Integer, String>();
		ArrayList<String> elementCates = new ArrayList<String>();
		ArrayList<String> conceptCates = new ArrayList<String>();
		String allstr = CommonUtil.concatJobItems(group.getItems());
		allstr = CommonUtil.filterWord(allstr);
		TreeMap<String, ArrayList<String>>  elementValues = Maps.newTreeMap();
		TreeMap<String, ArrayList<String>>  conceptValues = Maps.newTreeMap();
		for (int i = 0; i < group.getItems().size(); i++) {
			String content = group.getItems().get(i).getContent();
			if (null == content)
				continue;
			content = CommonUtil.filterWord(content);
			//分词
			List<String> segs = CommonUtil.segContent(content);
			segwords.add(Utils.concatWithEnd(segs, " "));
			ArrayList<String> elementCate = classify.classElement(segs, content,elementValues);
			ArrayList<String> concceptCate = classify.classConcept(segs, content,conceptValues);
			String classresult = CommonUtil.toCateResult(concceptCate,
					elementCate, content);
			catemaps.put(i, classresult);
			Utils.addCollection(conceptCates, concceptCate);
			Utils.addCollection(elementCates, elementCate);
		}
		List<String> segs = CommonUtil.segContent(allstr);
		String classresult = CommonUtil.toCateResult(conceptCates,
				elementCates, allstr);
		ParseConfig.logger.debug("分析到的概念" + conceptCates + "名字是"
				+ CommonUtil.toConceptName(conceptCates));
		ParseConfig.logger.debug("分析到的要素" + elementCates + "名字是"
				+ CommonUtil.toElementName(elementCates));
		catemaps.put(-1, classresult);
		ParseInfo info = classify.classOntology(catemaps, segs, conceptCates,
				elementCates,pcate_id,level);
		
		info.setConceptValueList(CommonUtil.toConceptValue(conceptValues));
		info.setElementValueList(CommonUtil.toElementValue(elementValues));
		
		info.setConcepts(CommonUtil.toConceptName(conceptCates));
		info.setElements(CommonUtil.toElementName(elementCates));
		List<String> Ontology = classify.getOntoPathByOntologyids(info.getCatlogs());
		info.setCatlogPath(Ontology);
		info.setSegwords(segwords);
		CommonUtil.innerCodeShared(info);
		CommonUtil.sortOntology(info);
		return info;
	}

	/**
	 * 	集合里面的数据，一条一条的分析
	 * 
	 * @param items
	 * @param pcateId
	 * @param level
	 * @return
	 */
	public ParseInfo parseJobItemListInorder(List<JobItem> items, String pcateId, int level) {
		
		ParseInfo parseInfo = new ParseInfo();
		Set<String> concepts = new HashSet<String>();
		Set<String> elements = new HashSet<String>();
		List<String> cutWords = new ArrayList<String>();
		List<String> matchInfos = new ArrayList<String>();
		
		for(int pos = 0;pos <items.size();pos++)
		{
			JobItem item = items.get(pos);
			ParseInfo info = parseJobItem(item,pcateId,level);
			ParseConfig.logger.debug(" 最终分析结果是"+info.toString());
			if(CollectionUtils.isNotEmpty(info.getConcepts()))
				concepts.addAll(info.getConcepts());
			if(CollectionUtils.isNotEmpty(info.getElements()))
				elements.addAll(info.getElements());
			if(CollectionUtils.isNotEmpty(info.getSegwords()))
				cutWords.addAll(info.getSegwords());
			if(CollectionUtils.isNotEmpty(info.getMatchInfos()))
				matchInfos.addAll(info.getMatchInfos());
			if(null!=info && CollectionUtils.isNotEmpty(info.getCatlogs()))
			{
				
				for(int i=0;i<info.getCatlogs().size();i++)
				{
					OntologyInfo ontologyInfo = new OntologyInfo(info.getCatlogs().get(i),info.getCatlognames().get(i) , 
														info.getCatlogPath().get(i), info.getWeights().get(i),
														info.getInner_code().get(i),info.getShared().get(i),info.getFactors().get(i),
														info.getExpressions(),pos);
					ontologyInfo.setPriority(ParseConfig.ontoWeight.get(info.getCatlogs()
							.get(i)));
					if(parseInfo.getOntologyInfos().contains(ontologyInfo))
					{
						for(OntologyInfo sonto :parseInfo.getOntologyInfos())
						{
							if(sonto.equals(ontologyInfo) )
							{
								if(ontologyInfo.getWeight()>sonto.getWeight())
									sonto.setWeight(ontologyInfo.getWeight());
								if(ontologyInfo.getFactor()>sonto.getFactor())
									sonto.setFactor(ontologyInfo.getFactor());
								sonto.getExpressions().addAll(ontologyInfo.getExpressions());
								sonto.getIds().addAll(ontologyInfo.getIds());
							}
						}
					}else{
						parseInfo.getOntologyInfos().add(ontologyInfo);
					}
				}
				
			}
		}
		Collections.sort(parseInfo.getOntologyInfos(), new OntoComp());
		parseInfo.getConcepts().addAll(concepts);
		parseInfo.getElements().addAll(elements);
		parseInfo.getSegwords().addAll(cutWords);
		parseInfo.getMatchInfos().addAll(matchInfos);
		return parseInfo;
	}

	public ParseInfo parseJobItemForOntology(JobItem item, String pcateId,
			int level) {

		ParseInfo parseInfo = new ParseInfo();
		Set<String> concepts = new HashSet<String>();
		Set<String> elements = new HashSet<String>();
		List<String> cutWords = new ArrayList<String>();
		List<String> matchInfos = new ArrayList<String>();
		
		ParseInfo info = parseJobItem(item, pcateId, level);
		ParseConfig.logger.debug(" 最终分析结果是"+info.toString());
		if(CollectionUtils.isNotEmpty(info.getConcepts()))
			concepts.addAll(info.getConcepts());
		if(CollectionUtils.isNotEmpty(info.getElements()))
			elements.addAll(info.getElements());
		if(CollectionUtils.isNotEmpty(info.getSegwords()))
			cutWords.addAll(info.getSegwords());
		if(CollectionUtils.isNotEmpty(info.getMatchInfos()))
			matchInfos.addAll(info.getMatchInfos());
		
		if (null != info && CollectionUtils.isNotEmpty(info.getCatlogs())) {
			for (int i = 0; i < info.getCatlogs().size(); i++) {
				OntologyInfo ontologyInfo = new OntologyInfo(info.getCatlogs()
						.get(i), info.getCatlognames().get(i), info
						.getCatlogPath().get(i), info.getWeights().get(i), info
						.getInner_code().get(i), info.getShared().get(i),info.getFactors().get(i) ,info
						.getExpressions());
				ontologyInfo.setPriority(ParseConfig.ontoWeight.get(info.getCatlogs()
						.get(i)));
				if (parseInfo.getOntologyInfos().contains(ontologyInfo)) {
					for (OntologyInfo sonto : parseInfo.getOntologyInfos()) {
						if (sonto.equals(ontologyInfo)) {
							if (ontologyInfo.getWeight() > sonto.getWeight())
								sonto.setWeight(ontologyInfo.getWeight());
						}
						sonto.getExpressions().addAll(
								ontologyInfo.getExpressions());
						sonto.getIds().addAll(ontologyInfo.getIds());
					}
				} else {
					parseInfo.getOntologyInfos().add(ontologyInfo);
				}
			}

		}
		Collections.sort(parseInfo.getOntologyInfos(), new OntoComp());
		parseInfo.getConcepts().addAll(concepts);
		parseInfo.getElements().addAll(elements);
		parseInfo.getSegwords().addAll(cutWords);
		parseInfo.getMatchInfos().addAll(matchInfos);
		return parseInfo;
	}
	
	
	class OntoComp implements Comparator<OntologyInfo>
	{

		@Override
		public int compare(OntologyInfo o1, OntologyInfo o2) {
			if(o2.getWeight()>o1.getWeight())
				return 1;
			else if(o2.getWeight()<o1.getWeight())	
				return -1;
			else
				return 0;
		}
		
	}
	
}
