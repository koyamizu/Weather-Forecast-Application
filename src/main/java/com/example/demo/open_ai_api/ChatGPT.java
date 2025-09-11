package com.example.demo.open_ai_api;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.ResponseFormat;
import org.springframework.stereotype.Component;

import com.example.demo.entity.LocationDataWrapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChatGPT{
		
	private final OpenAiChatModel chatModel;
	
	public LocationDataWrapper structureLocation(String input) {
		
		var outputConverter =new BeanOutputConverter<>(LocationDataWrapper.class);
		String jsonSchema=outputConverter.getJsonSchema();
		ChatResponse response=chatModel.call(
				new Prompt("""
						「%s」という場所について、次のように変換してください。
						【変換ルール】
						1. 通常の市町村 → 「市町村名-都道府県/州」。
						2. 政令市や区を持つ都市 → 「区名-市名-都道府県/州」。
						   - 例: 清水寺 → "東山区-京都市-京都府"。
						   - 例: タイムズスクエア → "マンハッタン区-ニューヨーク市-ニューヨーク州"。
						3. 大まかな地名（都道府県や州など） → 検索に用いた座標に基づき最も近い行政区を city とする。
						   - 例: 東京 → "新宿区-東京都"。
						   - 例: カリフォルニア → "サクラメント市-カリフォルニア州"。
						4. 同名の候補が存在する場合 → 配列にすべて格納する。

						【制約】
						- 緯度経度は必ず "緯度,経度" の文字列で、小数点第4位まで。
						- 必ず JSON のみを返す。余計な文章は出力しない。
						- 架空の地名や存在しない場所については、空配列を返すこと。
						""".formatted(input),
						OpenAiChatOptions.builder()
						.responseFormat(new ResponseFormat(ResponseFormat.Type.JSON_SCHEMA,jsonSchema))
						.build()
						)
				);
		String content=response.getResult().getOutput().getText();
		return outputConverter.convert(content);
	}
	
	public String translateAlert(String alertJson) {
		
		ChatResponse response=chatModel.call(
				new Prompt("以下に示す気象警報のJSONデータを解析し、日本語に翻訳してください。翻訳結果のみ出力すること「%s」"
						.formatted(alertJson),
						OpenAiChatOptions.builder().build()
						)
				);
		return response.getResult().getOutput().getText();
	}
}
