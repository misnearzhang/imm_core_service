package im.protoc.protocolbuf;


import java.util.UUID;

/**
 * Created by misnearzhang on 5/16/17.
 */
public class Test {
    public static void main(String[] args) {
        Protoc.Message.Builder builder = Protoc.Message.newBuilder();
        Protoc.Head.Builder head= Protoc.Head.newBuilder();
        head.setStatus(Protoc.status.DISCARD);
        head.setTime(System.currentTimeMillis());
        head.setType(Protoc.type.HANDSHAKE);
        head.setUid(UUID.randomUUID().toString());
        builder.setHead(head.build());
        builder.setBody("明年起逐步制定商用方案\n" +
                "实际上，中国联通在5G上也规划了时间表，其计划三个阶段推进5G发展。在2016年到2017年，主要是战略与架构制定阶段，发布5G需求的多种白皮书。而在2017年到2018年，是关键技术验证阶段，将拟定技术验证标准，诸如无线关键技术、核心网关键技术、传输关键技术。在2018年到2019年，是生态合作与网络准备阶段，将进行外场组网验证、制定商用建设方案、5G商业生态合作。\n" +
                "中国联通网络建设部副总经理马红兵认为，5G将与更多垂直行业结合，运营商将从语音服务向互联网与综合信息服务迁移，真正实现ICT与综合信息的无缝衔接。\n" +
                "已开始着手5G资金储备\n" +
                "而中国联通近些年的成绩单却不太乐观。自2014年开始，联通即出现了收入出现负增长，继而影响现金流及盈利。2016年全年，联通的净利润6.3亿元，同比下降94.1%。这一切，或许为联通开展5G蒙上了阴影。\n" +
                "中国联通董事长王晓初曾坦言，中国联通于4G时代建网的速度，远远落后于其他两大运营商，时间掌握出现重大错误。虽然5G技术还有待成熟，经营频率和技术标准有待明确，但中国联通从现在就要开始着手资金准备。该公司决定不派发2016年度末期息，为未来筹建5G网络做资金储备。\n" +
                "根据媒体报道，中国联通网络技术研究院在去年携手华为，全面展示了基于5G的智能驾驶应用业务。用户在驾驶的过程中，基于5G的车联网络不仅可以精准定位、实时反馈道路信息；还可以监测驾驶员的疲劳程度，做出风险驾驶的预警提示。\n" +
                "近日，广东联通又携手爱立信，在广州珠江新城开通国内首个5G商用网站点，同时也是全球首个基于双频段的5G点系统站点。通过三载波聚合，4X4 MIMO和256-QAM技术，峰值下载速率将由4G时代的375Mbps提升到1Gbps。\n" +
                "业内分析认为，此次与爱立信的联手，能够帮助中国联通高效实现2G、3G、4G的频谱资源整合并简化网络架构，使中国联通网络平滑演进到5G。" +
                "明年起逐步制定商用方案\n" +
                "实际上，中国联通在5G上也规划了时间表，其计划三个阶段推进5G发展。在2016年到2017年，主要是战略与架构制定阶段，发布5G需求的多种白皮书。而在2017年到2018年，是关键技术验证阶段，将拟定技术验证标准，诸如无线关键技术、核心网关键技术、传输关键技术。在2018年到2019年，是生态合作与网络准备阶段，将进行外场组网验证、制定商用建设方案、5G商业生态合作。\n" +
                "中国联通网络建设部副总经理马红兵认为，5G将与更多垂直行业结合，运营商将从语音服务向互联网与综合信息服务迁移，真正实现ICT与综合信息的无缝衔接。\n" +
                "已开始着手5G资金储备\n" +
                "而中国联通近些年的成绩单却不太乐观。自2014年开始，联通即出现了收入出现负增长，继而影响现金流及盈利。2016年全年，联通的净利润6.3亿元，同比下降94.1%。这一切，或许为联通开展5G蒙上了阴影。\n" +
                "中国联通董事长王晓初曾坦言，中国联通于4G时代建网的速度，远远落后于其他两大运营商，时间掌握出现重大错误。虽然5G技术还有待成熟，经营频率和技术标准有待明确，但中国联通从现在就要开始着手资金准备。该公司决定不派发2016年度末期息，为未来筹建5G网络做资金储备。\n" +
                "根据媒体报道，中国联通网络技术研究院在去年携手华为，全面展示了基于5G的智能驾驶应用业务。用户在驾驶的过程中，基于5G的车联网络不仅可以精准定位、实时反馈道路信息；还可以监测驾驶员的疲劳程度，做出风险驾驶的预警提示。\n" +
                "近日，广东联通又携手爱立信，在广州珠江新城开通国内首个5G商用网站点，同时也是全球首个基于双频段的5G点系统站点。通过三载波聚合，4X4 MIMO和256-QAM技术，峰值下载速率将由4G时代的375Mbps提升到1Gbps。\n" +
                "业内分析认为，此次与爱立信的联手，能够帮助中国联通高效实现2G、3G、4G的频谱资源整合并简化网络架构，使中国联通网络平滑演进到5G。" +
                "明年起逐步制定商用方案\n" +
                "实际上，中国联通在5G上也规划了时间表，其计划三个阶段推进5G发展。在2016年到2017年，主要是战略与架构制定阶段，发布5G需求的多种白皮书。而在2017年到2018年，是关键技术验证阶段，将拟定技术验证标准，诸如无线关键技术、核心网关键技术、传输关键技术。在2018年到2019年，是生态合作与网络准备阶段，将进行外场组网验证、制定商用建设方案、5G商业生态合作。\n" +
                "中国联通网络建设部副总经理马红兵认为，5G将与更多垂直行业结合，运营商将从语音服务向互联网与综合信息服务迁移，真正实现ICT与综合信息的无缝衔接。\n" +
                "已开始着手5G资金储备\n" +
                "而中国联通近些年的成绩单却不太乐观。自2014年开始，联通即出现了收入出现负增长，继而影响现金流及盈利。2016年全年，联通的净利润6.3亿元，同比下降94.1%。这一切，或许为联通开展5G蒙上了阴影。\n" +
                "中国联通董事长王晓初曾坦言，中国联通于4G时代建网的速度，远远落后于其他两大运营商，时间掌握出现重大错误。虽然5G技术还有待成熟，经营频率和技术标准有待明确，但中国联通从现在就要开始着手资金准备。该公司决定不派发2016年度末期息，为未来筹建5G网络做资金储备。\n" +
                "根据媒体报道，中国联通网络技术研究院在去年携手华为，全面展示了基于5G的智能驾驶应用业务。用户在驾驶的过程中，基于5G的车联网络不仅可以精准定位、实时反馈道路信息；还可以监测驾驶员的疲劳程度，做出风险驾驶的预警提示。\n" +
                "近日，广东联通又携手爱立信，在广州珠江新城开通国内首个5G商用网站点，同时也是全球首个基于双频段的5G点系统站点。通过三载波聚合，4X4 MIMO和256-QAM技术，峰值下载速率将由4G时代的375Mbps提升到1Gbps。\n" +
                "业内分析认为，此次与爱立信的联手，能够帮助中国联通高效实现2G、3G、4G的频谱资源整合并简化网络架构，使中国联通网络平滑演进到5G。" +
                "明年起逐步制定商用方案\n" +
                "实际上，中国联通在5G上也规划了时间表，其计划三个阶段推进5G发展。在2016年到2017年，主要是战略与架构制定阶段，发布5G需求的多种白皮书。而在2017年到2018年，是关键技术验证阶段，将拟定技术验证标准，诸如无线关键技术、核心网关键技术、传输关键技术。在2018年到2019年，是生态合作与网络准备阶段，将进行外场组网验证、制定商用建设方案、5G商业生态合作。\n" +
                "中国联通网络建设部副总经理马红兵认为，5G将与更多垂直行业结合，运营商将从语音服务向互联网与综合信息服务迁移，真正实现ICT与综合信息的无缝衔接。\n" +
                "已开始着手5G资金储备\n" +
                "而中国联通近些年的成绩单却不太乐观。自2014年开始，联通即出现了收入出现负增长，继而影响现金流及盈利。2016年全年，联通的净利润6.3亿元，同比下降94.1%。这一切，或许为联通开展5G蒙上了阴影。\n" +
                "中国联通董事长王晓初曾坦言，中国联通于4G时代建网的速度，远远落后于其他两大运营商，时间掌握出现重大错误。虽然5G技术还有待成熟，经营频率和技术标准有待明确，但中国联通从现在就要开始着手资金准备。该公司决定不派发2016年度末期息，为未来筹建5G网络做资金储备。\n" +
                "根据媒体报道，中国联通网络技术研究院在去年携手华为，全面展示了基于5G的智能驾驶应用业务。用户在驾驶的过程中，基于5G的车联网络不仅可以精准定位、实时反馈道路信息；还可以监测驾驶员的疲劳程度，做出风险驾驶的预警提示。\n" +
                "近日，广东联通又携手爱立信，在广州珠江新城开通国内首个5G商用网站点，同时也是全球首个基于双频段的5G点系统站点。通过三载波聚合，4X4 MIMO和256-QAM技术，峰值下载速率将由4G时代的375Mbps提升到1Gbps。\n" +
                "业内分析认为，此次与爱立信的联手，能够帮助中国联通高效实现2G、3G、4G的频谱资源整合并简化网络架构，使中国联通网络平滑演进到5G。" +
                "明年起逐步制定商用方案\n" +
                "实际上，中国联通在5G上也规划了时间表，其计划三个阶段推进5G发展。在2016年到2017年，主要是战略与架构制定阶段，发布5G需求的多种白皮书。而在2017年到2018年，是关键技术验证阶段，将拟定技术验证标准，诸如无线关键技术、核心网关键技术、传输关键技术。在2018年到2019年，是生态合作与网络准备阶段，将进行外场组网验证、制定商用建设方案、5G商业生态合作。\n" +
                "中国联通网络建设部副总经理马红兵认为，5G将与更多垂直行业结合，运营商将从语音服务向互联网与综合信息服务迁移，真正实现ICT与综合信息的无缝衔接。\n" +
                "已开始着手5G资金储备\n" +
                "而中国联通近些年的成绩单却不太乐观。自2014年开始，联通即出现了收入出现负增长，继而影响现金流及盈利。2016年全年，联通的净利润6.3亿元，同比下降94.1%。这一切，或许为联通开展5G蒙上了阴影。\n" +
                "中国联通董事长王晓初曾坦言，中国联通于4G时代建网的速度，远远落后于其他两大运营商，时间掌握出现重大错误。虽然5G技术还有待成熟，经营频率和技术标准有待明确，但中国联通从现在就要开始着手资金准备。该公司决定不派发2016年度末期息，为未来筹建5G网络做资金储备。\n" +
                "根据媒体报道，中国联通网络技术研究院在去年携手华为，全面展示了基于5G的智能驾驶应用业务。用户在驾驶的过程中，基于5G的车联网络不仅可以精准定位、实时反馈道路信息；还可以监测驾驶员的疲劳程度，做出风险驾驶的预警提示。\n" +
                "近日，广东联通又携手爱立信，在广州珠江新城开通国内首个5G商用网站点，同时也是全球首个基于双频段的5G点系统站点。通过三载波聚合，4X4 MIMO和256-QAM技术，峰值下载速率将由4G时代的375Mbps提升到1Gbps。\n" +
                "业内分析认为，此次与爱立信的联手，能够帮助中国联通高效实现2G、3G、4G的频谱资源整合并简化网络架构，使中国联通网络平滑演进到5G。" +
                "明年起逐步制定商用方案\n" +
                "实际上，中国联通在5G上也规划了时间表，其计划三个阶段推进5G发展。在2016年到2017年，主要是战略与架构制定阶段，发布5G需求的多种白皮书。而在2017年到2018年，是关键技术验证阶段，将拟定技术验证标准，诸如无线关键技术、核心网关键技术、传输关键技术。在2018年到2019年，是生态合作与网络准备阶段，将进行外场组网验证、制定商用建设方案、5G商业生态合作。\n" +
                "中国联通网络建设部副总经理马红兵认为，5G将与更多垂直行业结合，运营商将从语音服务向互联网与综合信息服务迁移，真正实现ICT与综合信息的无缝衔接。\n" +
                "已开始着手5G资金储备\n" +
                "而中国联通近些年的成绩单却不太乐观。自2014年开始，联通即出现了收入出现负增长，继而影响现金流及盈利。2016年全年，联通的净利润6.3亿元，同比下降94.1%。这一切，或许为联通开展5G蒙上了阴影。\n" +
                "中国联通董事长王晓初曾坦言，中国联通于4G时代建网的速度，远远落后于其他两大运营商，时间掌握出现重大错误。虽然5G技术还有待成熟，经营频率和技术标准有待明确，但中国联通从现在就要开始着手资金准备。该公司决定不派发2016年度末期息，为未来筹建5G网络做资金储备。\n" +
                "根据媒体报道，中国联通网络技术研究院在去年携手华为，全面展示了基于5G的智能驾驶应用业务。用户在驾驶的过程中，基于5G的车联网络不仅可以精准定位、实时反馈道路信息；还可以监测驾驶员的疲劳程度，做出风险驾驶的预警提示。\n" +
                "近日，广东联通又携手爱立信，在广州珠江新城开通国内首个5G商用网站点，同时也是全球首个基于双频段的5G点系统站点。通过三载波聚合，4X4 MIMO和256-QAM技术，峰值下载速率将由4G时代的375Mbps提升到1Gbps。\n" +
                "业内分析认为，此次与爱立信的联手，能够帮助中国联通高效实现2G、3G、4G的频谱资源整合并简化网络架构，使中国联通网络平滑演进到5G。" +
                "明年起逐步制定商用方案\n" +
                "实际上，中国联通在5G上也规划了时间表，其计划三个阶段推进5G发展。在2016年到2017年，主要是战略与架构制定阶段，发布5G需求的多种白皮书。而在2017年到2018年，是关键技术验证阶段，将拟定技术验证标准，诸如无线关键技术、核心网关键技术、传输关键技术。在2018年到2019年，是生态合作与网络准备阶段，将进行外场组网验证、制定商用建设方案、5G商业生态合作。\n" +
                "中国联通网络建设部副总经理马红兵认为，5G将与更多垂直行业结合，运营商将从语音服务向互联网与综合信息服务迁移，真正实现ICT与综合信息的无缝衔接。\n" +
                "已开始着手5G资金储备\n" +
                "而中国联通近些年的成绩单却不太乐观。自2014年开始，联通即出现了收入出现负增长，继而影响现金流及盈利。2016年全年，联通的净利润6.3亿元，同比下降94.1%。这一切，或许为联通开展5G蒙上了阴影。\n" +
                "中国联通董事长王晓初曾坦言，中国联通于4G时代建网的速度，远远落后于其他两大运营商，时间掌握出现重大错误。虽然5G技术还有待成熟，经营频率和技术标准有待明确，但中国联通从现在就要开始着手资金准备。该公司决定不派发2016年度末期息，为未来筹建5G网络做资金储备。\n" +
                "根据媒体报道，中国联通网络技术研究院在去年携手华为，全面展示了基于5G的智能驾驶应用业务。用户在驾驶的过程中，基于5G的车联网络不仅可以精准定位、实时反馈道路信息；还可以监测驾驶员的疲劳程度，做出风险驾驶的预警提示。\n" +
                "近日，广东联通又携手爱立信，在广州珠江新城开通国内首个5G商用网站点，同时也是全球首个基于双频段的5G点系统站点。通过三载波聚合，4X4 MIMO和256-QAM技术，峰值下载速率将由4G时代的375Mbps提升到1Gbps。\n" +
                "业内分析认为，此次与爱立信的联手，能够帮助中国联通高效实现2G、3G、4G的频谱资源整合并简化网络架构，使中国联通网络平滑演进到5G。" +
                "明年起逐步制定商用方案\n" +
                "实际上，中国联通在5G上也规划了时间表，其计划三个阶段推进5G发展。在2016年到2017年，主要是战略与架构制定阶段，发布5G需求的多种白皮书。而在2017年到2018年，是关键技术验证阶段，将拟定技术验证标准，诸如无线关键技术、核心网关键技术、传输关键技术。在2018年到2019年，是生态合作与网络准备阶段，将进行外场组网验证、制定商用建设方案、5G商业生态合作。\n" +
                "中国联通网络建设部副总经理马红兵认为，5G将与更多垂直行业结合，运营商将从语音服务向互联网与综合信息服务迁移，真正实现ICT与综合信息的无缝衔接。\n" +
                "已开始着手5G资金储备\n" +
                "而中国联通近些年的成绩单却不太乐观。自2014年开始，联通即出现了收入出现负增长，继而影响现金流及盈利。2016年全年，联通的净利润6.3亿元，同比下降94.1%。这一切，或许为联通开展5G蒙上了阴影。\n" +
                "中国联通董事长王晓初曾坦言，中国联通于4G时代建网的速度，远远落后于其他两大运营商，时间掌握出现重大错误。虽然5G技术还有待成熟，经营频率和技术标准有待明确，但中国联通从现在就要开始着手资金准备。该公司决定不派发2016年度末期息，为未来筹建5G网络做资金储备。\n" +
                "根据媒体报道，中国联通网络技术研究院在去年携手华为，全面展示了基于5G的智能驾驶应用业务。用户在驾驶的过程中，基于5G的车联网络不仅可以精准定位、实时反馈道路信息；还可以监测驾驶员的疲劳程度，做出风险驾驶的预警提示。\n" +
                "近日，广东联通又携手爱立信，在广州珠江新城开通国内首个5G商用网站点，同时也是全球首个基于双频段的5G点系统站点。通过三载波聚合，4X4 MIMO和256-QAM技术，峰值下载速率将由4G时代的375Mbps提升到1Gbps。\n" +
                "业内分析认为，此次与爱立信的联手，能够帮助中国联通高效实现2G、3G、4G的频谱资源整合并简化网络架构，使中国联通网络平滑演进到5G。" +
                "明年起逐步制定商用方案\n" +
                "实际上，中国联通在5G上也规划了时间表，其计划三个阶段推进5G发展。在2016年到2017年，主要是战略与架构制定阶段，发布5G需求的多种白皮书。而在2017年到2018年，是关键技术验证阶段，将拟定技术验证标准，诸如无线关键技术、核心网关键技术、传输关键技术。在2018年到2019年，是生态合作与网络准备阶段，将进行外场组网验证、制定商用建设方案、5G商业生态合作。\n" +
                "中国联通网络建设部副总经理马红兵认为，5G将与更多垂直行业结合，运营商将从语音服务向互联网与综合信息服务迁移，真正实现ICT与综合信息的无缝衔接。\n" +
                "已开始着手5G资金储备\n" +
                "而中国联通近些年的成绩单却不太乐观。自2014年开始，联通即出现了收入出现负增长，继而影响现金流及盈利。2016年全年，联通的净利润6.3亿元，同比下降94.1%。这一切，或许为联通开展5G蒙上了阴影。\n" +
                "中国联通董事长王晓初曾坦言，中国联通于4G时代建网的速度，远远落后于其他两大运营商，时间掌握出现重大错误。虽然5G技术还有待成熟，经营频率和技术标准有待明确，但中国联通从现在就要开始着手资金准备。该公司决定不派发2016年度末期息，为未来筹建5G网络做资金储备。\n" +
                "根据媒体报道，中国联通网络技术研究院在去年携手华为，全面展示了基于5G的智能驾驶应用业务。用户在驾驶的过程中，基于5G的车联网络不仅可以精准定位、实时反馈道路信息；还可以监测驾驶员的疲劳程度，做出风险驾驶的预警提示。\n" +
                "近日，广东联通又携手爱立信，在广州珠江新城开通国内首个5G商用网站点，同时也是全球首个基于双频段的5G点系统站点。通过三载波聚合，4X4 MIMO和256-QAM技术，峰值下载速率将由4G时代的375Mbps提升到1Gbps。\n" +
                "业内分析认为，此次与爱立信的联手，能够帮助中国联通高效实现2G、3G、4G的频谱资源整合并简化网络架构，使中国联通网络平滑演进到5G。" +
                "明年起逐步制定商用方案\n" +
                "实际上，中国联通在5G上也规划了时间表，其计划三个阶段推进5G发展。在2016年到2017年，主要是战略与架构制定阶段，发布5G需求的多种白皮书。而在2017年到2018年，是关键技术验证阶段，将拟定技术验证标准，诸如无线关键技术、核心网关键技术、传输关键技术。在2018年到2019年，是生态合作与网络准备阶段，将进行外场组网验证、制定商用建设方案、5G商业生态合作。\n" +
                "中国联通网络建设部副总经理马红兵认为，5G将与更多垂直行业结合，运营商将从语音服务向互联网与综合信息服务迁移，真正实现ICT与综合信息的无缝衔接。\n" +
                "已开始着手5G资金储备\n" +
                "而中国联通近些年的成绩单却不太乐观。自2014年开始，联通即出现了收入出现负增长，继而影响现金流及盈利。2016年全年，联通的净利润6.3亿元，同比下降94.1%。这一切，或许为联通开展5G蒙上了阴影。\n" +
                "中国联通董事长王晓初曾坦言，中国联通于4G时代建网的速度，远远落后于其他两大运营商，时间掌握出现重大错误。虽然5G技术还有待成熟，经营频率和技术标准有待明确，但中国联通从现在就要开始着手资金准备。该公司决定不派发2016年度末期息，为未来筹建5G网络做资金储备。\n" +
                "根据媒体报道，中国联通网络技术研究院在去年携手华为，全面展示了基于5G的智能驾驶应用业务。用户在驾驶的过程中，基于5G的车联网络不仅可以精准定位、实时反馈道路信息；还可以监测驾驶员的疲劳程度，做出风险驾驶的预警提示。\n" +
                "近日，广东联通又携手爱立信，在广州珠江新城开通国内首个5G商用网站点，同时也是全球首个基于双频段的5G点系统站点。通过三载波聚合，4X4 MIMO和256-QAM技术，峰值下载速率将由4G时代的375Mbps提升到1Gbps。\n" +
                "业内分析认为，此次与爱立信的联手，能够帮助中国联通高效实现2G、3G、4G的频谱资源整合并简化网络架构，使中国联通网络平滑演进到5G。");
        Protoc.Message message = builder.build();
        System.out.println(message.toByteArray().length);
    }
}
