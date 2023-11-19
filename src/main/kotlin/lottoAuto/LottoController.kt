package lottoAuto

import lottoAuto.domain.Lotto
import lottoAuto.domain.LottoFactory
import lottoAuto.domain.LottoNumber
import lottoAuto.domain.LottoRank
import lottoAuto.domain.LottoProfitCalculator
import lottoAuto.domain.toLottoNumber
import lottoAuto.domain.WinningLotto
import lottoAuto.view.InputView
import lottoAuto.view.OutputView

object LottoController {
    private const val LOTTO_PRICE = 1000

    fun getPurchaseAmount(): Int {
        return InputView.getPurchaseAmount()
    }

    fun getWinningLotto(bonusLottoNumber: LottoNumber): WinningLotto {
        return WinningLotto(
            winningLottoNumbers = InputView.getWinningNumbers().map { it.toLottoNumber() },
            bonusLottoNumber = bonusLottoNumber
        )
    }

    fun getBonusLottoNumber(): LottoNumber {
        val bonusNumber = InputView.getBonusNumber()
        return bonusNumber.toLottoNumber()
    }

    fun createLottoList(purchaseAmount: Int): List<Lotto> {
        val numOfLotto = purchaseAmount / LOTTO_PRICE
        OutputView.printNumOfLotto(numOfLotto)

        val lottoList = LottoFactory.create(numOfLotto)
        lottoList.forEach {
            val lottoNumbers = it.lottoNumbers.map { lottoNumber -> lottoNumber.number }
            OutputView.printLottoNumbers(lottoNumbers)
        }
        return lottoList
    }

    fun statistics(
        purchaseAmount: Int,
        lottoList: List<Lotto>,
        winningLotto: WinningLotto,
    ) {
        OutputView.printStatisticsHeader()
        val lottoRanks = winningLotto.rank(lottoList)
        val lottoRankGroup = lottoRanks.groupByLottoRank()
        lottoRankGroup
            .entries
            .forEach {
                if (it.key == LottoRank.BONUS) {
                    OutputView.printBonusNumberStatistics(it.key.matchCount, it.key.winningMoney, it.value)
                }
                OutputView.printLottoStatistics(it.key.matchCount, it.key.winningMoney, it.value)
            }

        val totalWinningMoney = lottoRanks.getTotalWinningMoney()
        val profit = LottoProfitCalculator.calcProfit(purchaseAmount, totalWinningMoney)
        OutputView.printProfitResult(profit.rateOfReturn, profit.resultMsg)
    }
}

fun main() {
    val purchaseAmount = LottoController.getPurchaseAmount()
    val lottoList = LottoController.createLottoList(purchaseAmount)
    val bonusLottoNumber = LottoController.getBonusLottoNumber()
    val winningLotto = LottoController.getWinningLotto(bonusLottoNumber)

    LottoController.statistics(purchaseAmount, lottoList, winningLotto)
}
