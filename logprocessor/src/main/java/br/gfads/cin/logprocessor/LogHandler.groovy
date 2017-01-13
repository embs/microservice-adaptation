package br.gfads.cin.logprocessor

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

import com.google.common.collect.HashBasedTable

import com.google.common.collect.LinkedListMultimap
import com.google.common.collect.Multimap
import com.google.common.collect.MultimapBuilder
import com.google.common.collect.Multimaps
import com.google.common.collect.Table

import groovy.json.JsonException
import groovy.json.JsonSlurper

class LogHandler {

	static void toCsv(String pathname, String csvPathname) {
		File log = new File(pathname)

		Scanner scanner = new Scanner(log)
		JsonSlurper slurper = new JsonSlurper()

		PrintWriter writer = new PrintWriter(new File(csvPathname))

		while(scanner.hasNext()) {
			Map map
			try {
				map = slurper.parseText(scanner.nextLine())
				map.message = slurper.parseText(map.message)
			}
			catch(JsonException e) { }
			finally {
				StringBuilder sb = new StringBuilder()
				map.each { key, value ->
					sb.append(key)
							.append(',')
					if(value instanceof Map) {
						sb.append(',')
						value.each { k, v ->
							sb.append(k)
									.append(',')
									.append(v)
									.append(',')
						}
					}
					else {
						sb.append(value)
								.append(',')
					}
				}
				if(sb.length() > 0)
					sb.setLength(sb.length()-1)
				sb.append('\n')
				writer.append(sb.toString())
			}
		}
		writer.flush()
		writer.close()
	}

	static List<Map> loadLog(String pathname) {
		File log = new File(pathname)

		Scanner scanner = new Scanner(log)
		List<Map> entries = new LinkedList<>()
		JsonSlurper slurper = new JsonSlurper()
		while(scanner.hasNext()) {
			Map map = slurper.parseText(scanner.nextLine())
			try {
				map.message = slurper.parseText(map.message)
			}
			catch(JsonException e) { }
			finally {
				entries << map
			}
		}

		return entries
	}

	static void createCsv(String pathname, List<Map> logParsed) {
		PrintWriter writer = new PrintWriter(new File(pathname))


		logParsed.each { Map map ->
			StringBuilder sb = new StringBuilder()
			map.each { key, value ->
				sb.append(key)
						.append(',')
				if(value instanceof Map) {
					sb.append(',')
					value.each { k, v ->
						sb.append(k)
								.append(',')
								.append(v)
								.append(',')
					}
				}
				else {
					sb.append(value)
							.append(',')
				}
			}
			sb.setLength(sb.length()-1)
			sb.append('\n')
			writer.append(sb.toString())
		}
		writer.flush()

		//		println sb.toString()
	}



	static def iterator(String pathname, Processor p) {
		File log = new File(pathname)

		Scanner scanner = new Scanner(log)
		JsonSlurper slurper = new JsonSlurper()
		while(scanner.hasNext()) {
			try {
				String s = scanner.nextLine()
				Map map = slurper.parseText(s)
				println s
				try {
					map.message = slurper.parseText(map.message)
				}
				catch(JsonException e) { }
				finally {
					p.execute(map)
				}
			}
			catch(JsonException f) { }
			finally {
				continue
			}
		}

	}

	public static void process(def path, def spec, def ip, def tpool) {

//		String adaptation = drive+":\\logs\\adaptation\\adaptation.log"
//		String planner = drive+":\\logs\\planner\\planner.log"
//		String reqresp = drive+":\\logs\\verifier\\reqresp\\property.log"
//		String resptime = drive+":\\logs\\verifier\\timeresp\\property.log"
//		String monolith = drive+":\\logs\\monolith\\adaptation.log"
		
		String PATH = path;
		String sp = File.separator; 
		
		String adaptation = PATH + "${sp}adaptation${sp}adaptation.log"
		println adaptation
		String planner =    PATH + "${sp}planner${sp}planner.log"
		println planner
		String reqresp =    PATH + "${sp}verifier${sp}reqresp${sp}property.log"
		println reqresp
		String resptime =   PATH + "${sp}verifier${sp}timeresp${sp}property.log"
		println resptime
		String monolith =   PATH + "${sp}monolith${sp}monolithic.log"
		println monolith

//		tpool.execute({
//			ServiceTime st = new ServiceTime()
//			iterator(resptime, st)
//			println "[${ip}:ms]-service time:" + st.avg()
//			st.toTxt("${ip}-ms-service_time.txt")
//			st.reset()
//		});
//
//		tpool.execute({
//			ServiceTime st = new ServiceTime()
//			iterator(monolith, st)
//			st.toTxt("${ip}-mo-service_time.txt")
//			println "[${ip}:mo]-service time:" + st.avg()
//			st.reset()
//		});
//		
		tpool.execute({
			AdaptationExecute ae = new AdaptationExecute(key:"execute-workflow-before-adapt")
			iterator(adaptation, ae)
			ae.toTxt("${ip}-ms-workflow.txt")
			println "[${ip}:ms]-workflow:"+ae.avg()
//			ae.reset()
//			ae.key = "execute-adaption"
//			iterator(adaptation, ae)
//			ae.toTxt("${ip}-ms-script_exeution.txt")
//			println "[${ip}:ms]-script execution:"+ae.avg()
//			ae.reset()
		})
//
//		tpool.execute({
//			Planner p = new Planner()
//			iterator(planner, p)
//			p.toTxt("${ip}-ms-planning.txt")
//			println "[${ip}:ms]-planning:"+p.avg()
//			p.reset()
//		})
//
//		tpool.execute({
//			ReqResp rr = new ReqResp()
//			iterator(reqresp,rr)
//			rr.toTxt("${ip}-ms-ltl_property.txt")
//			println "[${ip}:ms]-ltl property:"+rr.avg()
//			rr.reset()
//		})
//
//		tpool.execute({
//			RespTime rt = new RespTime()
//			iterator(resptime, rt)
//			rt.toTxt("${ip}-ms-quality_property.txt")
//			println "[${ip}:ms]-quality property:"+rt.avg()
//			rt.reset()
//		});
		tpool.execute({
			AdaptationExecute ae = new AdaptationExecute(key:"execute-workflow-before-adapt")
			ae.key = "execute-workflow-before-adapt"
			iterator(monolith, ae)
			ae.toTxt("${ip}-mo-workflow.txt")
			println "[${ip}:mo]-workflow:"+ae.avg()
//			ae.reset()
//			ae.key = "execute-adaption"
//			iterator(monolith, ae)
//			ae.toTxt("${ip}-mo-script_execution.txt")
//			println "[${ip}:mo]-script execution:"+ae.avg()
		})
//
//		tpool.execute({
//			Planner p = new Planner()
//			p = new Planner()
//			iterator(monolith, p)
//			p.toTxt("${ip}-mo-planning.txt")
//			println "[${ip}:mo]-planning:"+p.avg()
//		})
//
//		tpool.execute({
//			ReqResp rr = new ReqResp()
//			rr = new ReqResp()
//			iterator(monolith,rr)
//			rr.toTxt("${ip}-mo-ltl_property.txt")
//			println "[${ip}:mo]-ltl property:"+rr.avg()
//		})
//
//		tpool.execute({
//			RespTime rt = new RespTime()
//			rt = new RespTime()
//			iterator(monolith, rt)
//			rt.toTxt("${ip}-mo-quality_property.txt")
//			println "[${ip}:mo]-quality property:"+rt.avg()
//		})
		
		
	}
	
	
	public static void main(String[] args) {
		ExecutorService tpool = Executors.newCachedThreadPool()
		
		if(args.length > 0) {
			println args
			process(args[0], args[1], args[2],tpool)
			
		}
		else {
			process("C:\\Users\\adalr\\Desktop\\logs-splitted\\logs-min-client", "teste","teste", tpool)
		}
//		process("local","local",tpool)
		//		process("Y","10.66.66.32",tpool)
		tpool.shutdown()
		
	}
}
