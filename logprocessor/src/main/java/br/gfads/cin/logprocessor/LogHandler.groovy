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
			Map map = slurper.parseText(scanner.nextLine())
			try {
				map.message = slurper.parseText(map.message)
			}
			catch(JsonException e) { }
			finally {
				p.execute(map)
			}
		}

	}

	public static void process(def drive, def ip, def tpool) {

		String adaptation = drive+":\\logs\\adaptation\\adaptation.log"
		String planner = drive+":\\logs\\planner\\planner.log"
		String reqresp = drive+":\\logs\\verifier\\reqresp\\property.log"
		String resptime = drive+":\\logs\\verifier\\timeresp\\property.log"
		String monolith = drive+":\\logs\\monolith\\adaptation.log"


		tpool.execute({
			AdaptationExecute ae = new AdaptationExecute(key:"execute-workflow-before-adapt")
			iterator(adaptation, ae)
			println "[${ip}:ms]-script execution:"+ae.avg()
			ae.reset()
			ae.key = "execute-adaption"
			iterator(adaptation, ae)
			println "[${ip}:ms]-workflow:"+ae.avg()
			ae.reset()
		})

		tpool.execute({
			Planner p = new Planner()
			iterator(planner, p)
			println "[${ip}:ms]-planning:"+p.avg()
			p.reset()
		})

		tpool.execute({
			ReqResp rr = new ReqResp()
			iterator(reqresp,rr)
			println "[${ip}:ms]-ltl property:"+rr.avg()
			rr.reset()
		})

		tpool.execute({
			RespTime rt = new RespTime()
			iterator(resptime, rt)
			println "[${ip}:ms]-quality property:"+rt.avg()
			rt.reset()
		});

		tpool.execute({
			AdaptationExecute ae = new AdaptationExecute(key:"execute-workflow-before-adapt")
			ae.key = "execute-workflow-before-adapt"
			iterator(monolith, ae)
			println "[${ip}:mo]-script execution:"+ae.avg()
			ae.reset()
			ae.key = "execute-adaption"
			iterator(monolith, ae)
			println "[${ip}:mo]-workflow:"+ae.avg()
		})

		tpool.execute({
			Planner p = new Planner()
			p = new Planner()
			iterator(monolith, p)
			println "[${ip}:mo]-planning:"+p.avg()
		})

		tpool.execute({
			ReqResp rr = new ReqResp()
			rr = new ReqResp()
			iterator(monolith,rr)
			println "[${ip}:mo]-ltl property:"+rr.avg()
		})

		tpool.execute({
			RespTime rt = new RespTime()
			rt = new RespTime()
			iterator(monolith, rt)
			println "[${ip}:mo]-quality property:"+rt.avg()
		})
	}
	
	
	public static void main(String[] args) {
		ExecutorService tpool = Executors.newFixedThreadPool(4)
		
//		process("Y","10.66.66.32",tpool)
		process("Z","10.66.66.22",tpool)
		
		tpool.shutdown()
		
	}
}