package lottoAuto.domain

object LottoFactory {
    fun create(numOfLotto: Int): List<Lotto> {
        return List(numOfLotto) { Lotto(createShuffledLottoNumbers()) }
    }

    private fun createShuffledLottoNumbers(): List<LottoNumber> {
        return (LottoNumber.MIN_NUMBER..LottoNumber.MAX_NUMBER)
            .shuffled()
            .take( Lotto.LOTTO_SIZE )
            .map { LottoNumber.from(it) }
    }
}
