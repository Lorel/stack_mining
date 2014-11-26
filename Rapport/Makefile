all: clean
	pdflatex model.tex
	bibtex model
	pdflatex model.tex
	pdflatex model.tex

clean:
	rm -f *.aux *.out *.synctex.gz *.toc
